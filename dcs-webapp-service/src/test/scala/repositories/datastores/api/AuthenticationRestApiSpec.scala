package repositories.datastores.api

import java.util.Date

import io.vertx.scala.core.eventbus.EventBus
import io.vertx.servicediscovery.ServiceDiscovery
import it.unibo.dcs.commons.service.HttpEndpointDiscoveryImpl
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationRestApi
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
import rx.{Single, Subscriber}

class AuthenticationRestApiSpec extends FlatSpec with MockFactory with OneInstancePerTest {
  val user = User("niklegend", "nicola", "piscaglia", "bla", visible = true, new Date())
  val registerRequest = RegisterUserRequest(user.username, "password", user.firstName,
    user.lastName, user.bio, visible = true)
  val registeredSubscriber: Subscriber[Boolean] = stub[Subscriber[Boolean]]
  val authRestApi = new AuthenticationRestApi(new HttpEndpointDiscoveryImpl(mock[ServiceDiscovery], mock[EventBus]))

  /*it should "register a new user if it doesn't exits" in {
    // Given
    // userRepository is called with `request` as parameter returns an observable that contains only `user`
    (authRestApi registerUser  _) expects registerRequest returns (Single just true)

    // When
    // createUserUseCase is executed with argument `request`
    authRestApi registerUser registerRequest subscribe registeredSubscriber

    // Then
    // Verify that `subscriber.onNext` has been called once with `user` as argument
    (registeredSubscriber onNext _) verify true once()
    // Verify that `subscriber.onCompleted` has been called once
    (registeredSubscriber onCompleted: () => Unit) verify() once()
  }*/
}
