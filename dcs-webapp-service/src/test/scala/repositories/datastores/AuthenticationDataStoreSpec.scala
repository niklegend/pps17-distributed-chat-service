package repositories.datastores

import java.util.Date

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.interaction.Requests.{LoginUserRequest, RegisterUserRequest}
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.impl.AuthenticationDataStoreNetwork
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
import rx.lang.scala.{Observable, Subscriber}

import scala.language.postfixOps

class AuthenticationDataStoreSpec extends FlatSpec with MockFactory with OneInstancePerTest {

  private val authApi: AuthenticationApi = mock[AuthenticationApi]
  private val dataStore: AuthenticationDataStore = new AuthenticationDataStoreNetwork(authApi)
  private val user = User("niklegend", "nicola", "piscaglia", "bla", visible = true, new Date())
  private val registerRequest = RegisterUserRequest(user.username, "password", user.firstName,
    user.lastName)
  private val loginUserRequest = LoginUserRequest(user.username, "password")
  private val registeredSubscriber: Subscriber[String] = stub[Subscriber[String]]
  private val loginSubscriber: Subscriber[String] = stub[Subscriber[String]]
  private val logoutSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
  private val token = "token"


  it should "register a new user" in {
    // Given
    (authApi registerUser _) expects registerRequest returns (Observable just token)

    // When
    dataStore.registerUser(registerRequest).subscribe(registeredSubscriber)

    // Then
    // Verify that `subscriber.onNext` has been called once with `token` as argument
    (registeredSubscriber onNext _) verify token once()
    // Verify that `subscriber.onCompleted` has been called once
    (() => registeredSubscriber onCompleted) verify() once()
  }


  it should "authenticate a registered user" in {
    // Given
    (authApi registerUser _) expects registerRequest returns (Observable just token)
    (authApi loginUser _) expects loginUserRequest returns (Observable just token)

    // When
    dataStore.registerUser(registerRequest).subscribe(_ => {
      dataStore.loginUser(loginUserRequest).subscribe(loginSubscriber)
    })

    // Then
    // Verify that `subscriber.onNext` has been called once with `token` as argument
    (loginSubscriber onNext _) verify token once()
    // Verify that `subscriber.onCompleted` has been called once
    (() => loginSubscriber onCompleted) verify() once()
  }


  it should "logout a logged user" in {
    // Given
    (authApi loginUser _) expects loginUserRequest returns (Observable just token)
    (authApi logoutUser _) expects user.username returns Observable.empty

    // When
    dataStore.loginUser(loginUserRequest).subscribe(_ => {
      dataStore.logoutUser(user.username).subscribe(logoutSubscriber)
    })

    // Then
    // Verify that `subscriber.onCompleted` has been called once
    (() => logoutSubscriber onCompleted) verify() once()
  }


}
