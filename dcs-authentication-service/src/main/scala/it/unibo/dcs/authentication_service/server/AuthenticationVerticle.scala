package it.unibo.dcs.authentication_service.server

import io.vertx.core
import io.vertx.core.{AbstractVerticle, Context}
import io.vertx.core.http.HttpMethod
import io.vertx.scala.ext.auth.{AuthProvider, KeyStoreOptions}
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTAuthOptions}
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.{BodyHandler, RedirectAuthHandler}
import it.unibo.dcs.authentication_service.interactor.{LoginUserUseCase, LogoutUserUseCase, RegisterUserUseCase}
import it.unibo.dcs.authentication_service.repository.AuthenticationRepository
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.interactor.ThreadExecutorExecutionContext
import it.unibo.dcs.commons.interactor.executor.PostExecutionThread
import it.unibo.dcs.commons.service.ServiceVerticle

import scala.io.Source

class AuthenticationVerticle(authenticationRepository: AuthenticationRepository) extends ServiceVerticle {

  private var host: String = "127.0.0.1" //will get overwritten when 'init' gets called
  private var port: Int = 0 //will get overwritten when 'init' gets called

  override protected def initializeRouter(router: Router): Unit = {
    val authProvider = createJwtAuthProvider()
    router.route().handler(BodyHandler.create())
    setupRedirects(router, authProvider)
    setupRoutes(router, authProvider)
  }

  override def init(jVertx: core.Vertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(jVertx, context, verticle)
    val config = context.config
    host = config.getString("host")
    port = config.getInteger("port")
  }

  override def start(): Unit = startHttpServer(host, port)

  private def createJwtAuthProvider(): JWTAuth = {
    val keyStoreSecret = Source.fromFile("keystore-secret.txt").getLines().next()
    val authOptions = KeyStoreOptions().setPath("keystore.jceks").setPassword(keyStoreSecret)
    val config = JWTAuthOptions().setKeyStore(authOptions)
    JWTAuth.create(vertx, config)
  }

  private def setupRedirects(router: Router, authProvider: AuthProvider): Unit = {
    val handler = RedirectAuthHandler.create(authProvider, "/")
    val route = router.route("/protected/*")
    route.method(HttpMethod.GET).handler(handler)
    route.method(HttpMethod.HEAD).handler(handler)
    route.method(HttpMethod.PATCH).handler(handler)
    route.method(HttpMethod.POST).handler(handler)
    route.method(HttpMethod.PUT).handler(handler)
    route.method(HttpMethod.DELETE).handler(handler)
  }

  private def setupRoutes(router: Router, jwtAuth: JWTAuth): Unit = {
    val requestHandler = getRequestHandler(jwtAuth)
    router.post("/register").handler(requestHandler.handleRegistration(_))
    router.post("/login").handler(requestHandler.handleLogin(_))
    router.post("/protected/logout").handler(requestHandler.handleLogout(_))
  }

  private def getRequestHandler(jwtAuth: JWTAuth): ServiceRequestHandler = {
    val threadExecutor = ThreadExecutorExecutionContext(vertx)
    val postExecutionThread = PostExecutionThread(RxHelper.scheduler(this.ctx))
    val loginUseCase = new LoginUserUseCase(threadExecutor, postExecutionThread, authenticationRepository, jwtAuth)
    val logoutUseCase = new LogoutUserUseCase(threadExecutor, postExecutionThread, authenticationRepository)
    val registerUseCase = new RegisterUserUseCase(threadExecutor, postExecutionThread, authenticationRepository, jwtAuth)
    new ServiceRequestHandlerImpl(loginUseCase, logoutUseCase, registerUseCase)
  }
}
