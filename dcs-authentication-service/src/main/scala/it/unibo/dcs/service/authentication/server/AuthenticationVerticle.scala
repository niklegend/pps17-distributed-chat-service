package it.unibo.dcs.service.authentication.server

import io.vertx.core
import io.vertx.core.{AbstractVerticle, Context}
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTAuthOptions}
import io.vertx.scala.ext.web.handler.{BodyHandler, JWTAuthHandler}
import io.vertx.scala.ext.web.{Router, RoutingContext}
import it.unibo.dcs.service.authentication.repository.AuthenticationRepository
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.VertxWebHelper._
import it.unibo.dcs.commons.interactor.ThreadExecutorExecutionContext
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.commons.service.{HttpEndpointPublisher, ServiceVerticle}
import it.unibo.dcs.service.authentication.authentication.{LoginValidator, LogoutUserValidator, RegistrationValidator}
import it.unibo.dcs.service.authentication.interactor.usecases._
import it.unibo.dcs.service.authentication.interactor.validations._
import it.unibo.dcs.service.authentication.server.containers.{UseCaseContainer, ValidationContainer}

import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

/** Verticle that runs the Authentication Service */
final class AuthenticationVerticle(private[this] val authenticationRepository: AuthenticationRepository,
                                   private[this] val publisher: HttpEndpointPublisher) extends ServiceVerticle {

  private var host: String = _
  private var port: Int = _

  override protected def initializeRouter(router: Router): Unit = {
    router.route().handler(BodyHandler.create())
    setupCors(router)
    val authProvider = JWTAuth.create(vertx, createJwtAuthOptions())
    setupProtectedRoutes(router, authProvider)
    setupRoutes(router, authProvider)
  }

  override def init(jVertx: core.Vertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(jVertx, context, verticle)
    host = config getString "host"
    port = config getInteger "port"
  }

  override def start(): Unit = startHttpServer(host, port)
    .doOnCompleted(
      publisher.publish(name = "authentication-service", host = host, port = port)
        .subscribe(record => log.info(s"${record.getName} record published!"),
          log.error(s"Could not publish record", _)))
    .subscribe(server => log.info(s"Server started at http://$host:${server.actualPort}"),
      log.error(s"Could not start server at http://$host:$port", _))

  private def createJwtAuthOptions(): JWTAuthOptions = {
    val keyStoreSecret = Source.fromResource("keystore-secret.txt").getLines().next()
    val keyStoreOptions = Json.obj(("type", "jceks"), ("path", "keystore.jceks"), ("password", keyStoreSecret))
    JWTAuthOptions.fromJson(Json.obj(("keyStore", keyStoreOptions)))
  }

  private def setupProtectedRoutes(router: Router, jwtAuth: JWTAuth): Unit = {
    val jwtAuthHandler = JWTAuthHandler.create(jwtAuth)
    val protectedRouter = Router.router(vertx)
    protectedRouter.route("/*").handler(checkTokenValidity(jwtAuthHandler, jwtAuth)(_))
    router.mountSubRouter("/protected", protectedRouter)
  }

  private def checkTokenValidity(jwtAuthHandler: JWTAuthHandler, jwtAuth: JWTAuth)
                                (implicit context: RoutingContext): Unit = {
    val token = getTokenFromHeader
    token.fold[Unit](() => returnError())(tokenValue => {
      isTokenValid(jwtAuth, tokenValue).onComplete {
        case Success(true) => checkTokenValidityInDb(tokenValue)
        case Failure(_) => returnError()
      }})

    def isTokenValid(jwtAuth: JWTAuth, token: String): Future[Boolean] =
    jwtAuth.authenticateFuture(Json.obj(("jwt", token))).map(user => user != null)

    def checkTokenValidityInDb(token: String)(implicit context: RoutingContext): Unit =
    authenticationRepository.isTokenValid(token).subscribe(tokenValid => if (tokenValid) {
        context.next()
      } else {
      returnError()
      }, _ => returnError())

    def returnError(): Unit =  respondWithCode(401)
  }

  private def setupRoutes(router: Router, jwtAuth: JWTAuth): Unit = {
    val requestHandler = getRequestHandler(jwtAuth)
    router.post("/register")
      .consumes("application/json").produces("application/json")
      .handler(requestHandler.handleRegistration(_))

    router.post("/login")
      .consumes("application/json").produces("application/json")
      .handler(requestHandler.handleLogin(_))

    router.post("/protected/logout").produces("application/json")
      .handler(requestHandler.handleLogout(_))

    router.get("/protected/tokenValidity").produces("application/json")
      .handler(requestHandler.handleTokenCheck(_))
  }

  private def getRequestHandler(jwtAuth: JWTAuth): ServiceRequestHandler = {
    val threadExecutor = ThreadExecutorExecutionContext(vertx)
    val postExecutionThread = PostExecutionThread(RxHelper.scheduler(this.ctx))

    val useCases = createUseCases(threadExecutor, postExecutionThread, jwtAuth)
    val validations = createValidations(threadExecutor, postExecutionThread)
    ServiceRequestHandlerImpl(useCases, validations)
  }

  private def createUseCases(threadExecutor: ThreadExecutor,
                             postExecutionThread: PostExecutionThread, jwtAuth: JWTAuth): UseCaseContainer = {
    val loginUseCase = LoginUserUseCase(threadExecutor, postExecutionThread, authenticationRepository, jwtAuth)
    val logoutUseCase = LogoutUserUseCase(threadExecutor, postExecutionThread, authenticationRepository)
    val registerUseCase = RegisterUserUseCase(threadExecutor, postExecutionThread, authenticationRepository, jwtAuth)
    val checkTokenUseCase = CheckTokenUseCase(threadExecutor, postExecutionThread, authenticationRepository)
     UseCaseContainer(loginUseCase, logoutUseCase, registerUseCase, checkTokenUseCase)
  }

  private def createValidations(threadExecutor: ThreadExecutor,
                                postExecutionThread: PostExecutionThread): ValidationContainer = {
    val logoutValidator = LogoutUserValidator(authenticationRepository)
    val logoutUserValidation = LogoutUserValidation(threadExecutor, postExecutionThread, logoutValidator)
    val registrationValidation = RegisterUserValidation(threadExecutor, postExecutionThread, RegistrationValidator())
    val loginValidator = LoginValidator(authenticationRepository)
    val loginValidation = LoginUserValidation(threadExecutor, postExecutionThread, loginValidator)
    ValidationContainer(logoutUserValidation, registrationValidation, loginValidation)
  }
}

object AuthenticationVerticle {
  def apply(authenticationRepository: AuthenticationRepository, publisher: HttpEndpointPublisher) =
    new AuthenticationVerticle(authenticationRepository, publisher)
}
