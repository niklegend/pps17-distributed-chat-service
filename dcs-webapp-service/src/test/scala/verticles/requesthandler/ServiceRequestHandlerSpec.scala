package verticles.requesthandler

import java.util.Date

import io.vertx.lang.scala.ScalaLogger
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, RoomRepository, UserRepository}
import it.unibo.dcs.service.webapp.verticles.requesthandler.ServiceRequestHandler
import it.unibo.dcs.service.webapp.verticles.requesthandler.impl.ServiceRequestHandlerImpl
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers, OneInstancePerTest}


class ServiceRequestHandlerSpec extends FlatSpec with MockFactory with Matchers with OneInstancePerTest {

  private val logger = ScalaLogger.getLogger(getClass.getName)

  private val userRepository: UserRepository = mock[UserRepository]
  private val authRepository: AuthenticationRepository = mock[AuthenticationRepository]
  private val roomRepository: RoomRepository = mock[RoomRepository]

  private val user = User("niklegend", "Nicola", "Piscaglia", "bio", visible = true, new Date)
  private val registerRequest = RegisterUserRequest(user.username, "password", user.firstName, user.lastName)
  private val token = "token"

  private val serviceRequestHandler: ServiceRequestHandler =
    new ServiceRequestHandlerImpl(userRepository, authRepository, roomRepository)

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
