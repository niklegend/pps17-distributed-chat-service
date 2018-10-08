package verticles.requesthandler

import java.util.Date

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.ScalaLogger
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.{Vertx, VertxOptions}
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.Implicits._
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.verticles.WebAppVerticle
import it.unibo.dcs.service.webapp.verticles.requesthandler.ServiceRequestHandler
import it.unibo.dcs.service.webapp.verticles.requesthandler.impl.ServiceRequestHandlerImpl
import it.unibo.dcs.service.webapp.verticles.utils.DeploymentUtils._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers, OneInstancePerTest}
import rx.lang.scala.Observable


class ServiceRequestHandlerSpec extends FlatSpec with MockFactory with Matchers with OneInstancePerTest {

  private val logger = ScalaLogger.getLogger(getClass.getName)

  val userRepository: UserRepository = mock[UserRepository]
  val authRepository: AuthenticationRepository = mock[AuthenticationRepository]

  val user = User("niklegend", "Nicola", "Piscaglia", "bio", visible = true, new Date)
  val registerRequest = RegisterUserRequest(user.username, "password", user.firstName, user.lastName)
  val token = "token"

  val serviceRequestHandler: ServiceRequestHandler = new ServiceRequestHandlerImpl(userRepository, authRepository)

  it should "register a new user when POST /register is called" in {
    /*
    VertxHelper.toObservable[Vertx](Vertx.clusteredVertx(VertxOptions(), _))
      .concatMap(vertx => Observable[Vertx](_ => {
        vertx.deployVerticle(nameForVerticle[WebAppVerticle], deploymentOptions)
        vertx
      })).toBlocking.subscribe(vertx => {
      val client = vertx.createHttpClient
      val req = client.post(deploymentPort, deploymentHost, "/api/register")

      req.exceptionHandler((err) => fail(err.getMessage))
      req.handler((resp) => assert(resp.statusCode == HttpResponseStatus.OK.code()))

      val requestBody: JsonObject = registerRequest
      req.end(requestBody)
    })*/
  }
}
