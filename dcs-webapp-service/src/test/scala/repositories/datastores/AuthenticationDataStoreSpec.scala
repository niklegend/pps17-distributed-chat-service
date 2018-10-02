package repositories.datastores

import java.util.Date

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.impl.AuthenticationDataStoreNetwork
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
import rx.{Single, Subscriber}

class AuthenticationDataStoreSpec extends FlatSpec with MockFactory with OneInstancePerTest {

  def fixture =
    new {
      val authApi: AuthenticationApi = mock[AuthenticationApi]
      val dataStore: AuthenticationDataStore = new AuthenticationDataStoreNetwork(authApi)
      val user = User("niklegend", "nicola", "piscaglia", "bla", visible = true, new Date())
      val registerRequest = RegisterUserRequest(user.username, "password", user.firstName,
        user.lastName, user.bio, visible = true)
      val registeredSubscriber: Subscriber[Boolean] = stub[Subscriber[Boolean]]
    }


  it should "register a new user if it doesn't exist" in {
    // Given
    (fixture.authApi registerUser _) expects fixture.registerRequest returns (Single just true)

    // When
    registerNewUser().subscribe(fixture.registeredSubscriber)

    // Then
    // Verify that `subscriber.onNext` has been called once with `user` as argument
    (fixture.registeredSubscriber onNext _) verify true once()
    // Verify that `subscriber.onCompleted` has been called once
    (fixture.registeredSubscriber onCompleted: () => Unit) verify() once()
  }


  /*
  it should "authenticate a registered user" in {
    val user = fixture.user
    val dataStore = fixture.dataStore
    registerNewUser().subscribe(_ => {
      dataStore.loginUser(user.username, "password")
        .subscribe(logged => assert(logged))
    })
  }*/

  private def registerNewUser(): Single[Boolean] = {
    val dataStore = fixture.dataStore
    dataStore.registerUser(fixture.registerRequest)
  }
}
