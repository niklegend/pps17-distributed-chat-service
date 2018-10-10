package repositories.datastores

import java.util.Date

import it.unibo.dcs.service.webapp.model.User
<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests.{LoginUserRequest, RegisterUserRequest}
=======
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.impl.AuthenticationDataStoreNetwork
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
<<<<<<< HEAD
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


=======
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
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
