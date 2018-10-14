package it.unibo.dcs.authentication_service.server

import io.vertx.core
import io.vertx.core.http.HttpMethod._
import io.vertx.core.{AbstractVerticle, Context}
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTAuthOptions}
import io.vertx.scala.ext.web.handler.{BodyHandler, CorsHandler, JWTAuthHandler}
import io.vertx.scala.ext.web.{Router, RoutingContext}
import io.vertx.scala.ext.web.handler.{BodyHandler, JWTAuthHandler}
import it.unibo.dcs.authentication_service.interactor.{CheckTokenUseCase, LoginUserUseCase, LogoutUserUseCase, RegisterUserUseCase}
import it.unibo.dcs.authentication_service.repository.AuthenticationRepository
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.VertxWebHelper._
import it.unibo.dcs.commons.interactor.ThreadExecutorExecutionContext
import it.unibo.dcs.commons.interactor.executor.PostExecutionThread
import it.unibo.dcs.commons.service.{HttpEndpointPublisher, ServiceVerticle}
import it.unibo.dcs.commons.VertxWebHelper._
import rx.lang.scala.Subscriber
import scala.io.Source
import scala.util.{Failure, Success}

final class AuthenticationVerticle(authenticationRepository: AuthenticationRepository, private[this] val publisher: HttpEndpointPublisher)
  extends ServiceVerticle {

  private var host: String = _
  private var port: Int = _

  override protected def initializeRouter(router: Router): Unit = {
    router.route()
      .handler(BodyHandler.create())

    router.route().handler(CorsHandler.create("*")
      .allowedMethod(GET)
      .allowedMethod(POST)
      .allowedMethod(PATCH)
      .allowedMethod(PUT)
      .allowedMethod(DELETE)
      .allowedHeader("Access-Control-Allow-Method")
      .allowedHeader("Access-Control-Allow-Origin")
      .allowedHeader("Access-Control-Allow-Credentials")
      .allowedHeader("Content-Type"))

    val authProvider = createJwtAuthProvider()
    router.route().handler(BodyHandler.create())
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
    protectedRouter.route("/*").handler(context => {
      val token = getTokenFromHeader(context)
      if (token.isEmpty) {
        respondWithCode(401)(context)
      } else{
        authenticationRepository.isTokenValid(token.get).subscribe(getTokenSubscriber(jwtAuthHandler, jwtAuth)(context))
      }
    })
    router.mountSubRouter("/protected", protectedRouter)
  }

  private def getTokenSubscriber(jwtAuthHandler: JWTAuthHandler, jwtAuth: JWTAuth)
                                (implicit context: RoutingContext): Subscriber[Boolean] = {
    new Subscriber[Boolean]() {
      override def onNext(tokenValid: Boolean): Unit = {
        if (tokenValid) {
          checkTokenValidity(jwtAuthHandler, jwtAuth)
        } else {
          respondWithCode(401)
        }
      }
      override def onError(error: Throwable): Unit =
        respondWithCode(401)
    }
  }

  private def checkTokenValidity(jwtAuthHandler: JWTAuthHandler, jwtAuth: JWTAuth)
                                (implicit context: RoutingContext): Unit = {
    jwtAuth.authenticateFuture(Json.obj(("jwt", getTokenFromHeader))).onComplete{ //TODO: solve the fact that the callback never gets called
      case Success(_) => context.next()
      case Failure(_) => respondWithCode(401)
    }
  }

  private def setupRoutes(router: Router, jwtAuth: JWTAuth): Unit = {
    val requestHandler = getRequestHandler(jwtAuth)
    router.post("/register").handler(requestHandler.handleRegistration(_))
    router.post("/login").handler(requestHandler.handleLogin(_))
    router.post("/protected/logout").handler(requestHandler.handleLogout(_))
    router.get("/protected/tokenValidity").handler(requestHandler.handleTokenCheck(_))
  }

  private def getRequestHandler(jwtAuth: JWTAuth): ServiceRequestHandler = {
    val threadExecutor = ThreadExecutorExecutionContext(vertx)
    val postExecutionThread = PostExecutionThread(RxHelper.scheduler(this.ctx))
    val loginUseCase = LoginUserUseCase(threadExecutor, postExecutionThread, authenticationRepository, jwtAuth)
    val logoutUseCase = LogoutUserUseCase(threadExecutor, postExecutionThread, authenticationRepository)
    val registerUseCase = RegisterUserUseCase(threadExecutor, postExecutionThread, authenticationRepository, jwtAuth)
    val checkTokenUseCase = CheckTokenUseCase(threadExecutor, postExecutionThread, authenticationRepository)
    ServiceRequestHandlerImpl(loginUseCase, logoutUseCase, registerUseCase, checkTokenUseCase)
  }
}

object AuthenticationVerticle {
  def apply(authenticationRepository: AuthenticationRepository, publisher: HttpEndpointPublisher) =
    new AuthenticationVerticle(authenticationRepository, publisher)
}
