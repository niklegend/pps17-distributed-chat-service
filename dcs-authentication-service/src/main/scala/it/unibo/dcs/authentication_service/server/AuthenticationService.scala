package it.unibo.dcs.authentication_service.server

import io.vertx.core.http.HttpMethod
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.{AuthProvider, KeyStoreOptions}
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTAuthOptions}
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.{BodyHandler, RedirectAuthHandler}
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

class AuthenticationService extends ServiceVerticle {

  override def startFuture(): Future[_] = {
    val authProvider = createJwtAuthProvider()
    _router.route().handler(BodyHandler.create())
    setupRedirects(_router, authProvider)
    setupRoutes(_router)
    createServer(_router, vertx)
  }

  private def createJwtAuthProvider(): AuthProvider = {
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

  private def createServer(router: Router, vertx: Vertx)(implicit context: ExecutionContext): Future[_] =
    vertx.createHttpServer()
      .requestHandler(router.accept(_))
      .listenFuture(8080, "0.0.0.0")
      .map { _ => println("Service started") }

  private def setupRoutes(router: Router): Unit = {
    val requestHandler: ServiceRequestHandler = new ServiceRequestHandlerImpl()
    router.post("/register").handler(requestHandler.handleRegistration(_))
    router.post("/login").handler(requestHandler.handleLogin(_))
    router.post("/protected/logout").handler(requestHandler.handleLogout(_))
  }
}

object Main extends App {
  Vertx vertx() deployVerticle new AuthenticationService()
}
