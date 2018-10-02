package repositories.datastores

import java.util.Date

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.impl.AuthenticationDataStoreNetwork
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.{Observable, Subscriber}

class AuthenticationDataStoreSpec extends FlatSpec with MockFactory {

  val fixture =
    new {
      val authApi: AuthenticationApi = mock[AuthenticationApi]
      val dataStore: AuthenticationDataStore = new AuthenticationDataStoreNetwork(authApi)
      val user = User("niklegend", "nicola", "piscaglia", "bla", visible = true, new Date())
      val registerRequest = RegisterUserRequest(user.username, "password", user.firstName,
        user.lastName)
      val registeredSubscriber: Subscriber[String] = stub[Subscriber[String]]
      val token = "token"
    }


  it should "register a new user" in {
    // Given
    (fixture.authApi registerUser _) expects fixture.registerRequest returns (Observable just fixture.token)

    // When
    fixture.dataStore.registerUser(fixture.registerRequest).subscribe(fixture.registeredSubscriber)

    // Then
    // Verify that `subscriber.onNext` has been called once with `user` as argument
    (fixture.registeredSubscriber onNext _) verify fixture.token once()
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

}
