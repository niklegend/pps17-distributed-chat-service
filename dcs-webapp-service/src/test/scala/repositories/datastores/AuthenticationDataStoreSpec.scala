package repositories.datastores

import java.util.Date

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import org.scalatest.FlatSpec
import rx.Single

class AuthenticationDataStoreSpec extends FlatSpec {

  def fixture =
    new {
      val dataStore: AuthenticationDataStore = new AuthenticationDataStore {
        override def loginUser(username: String, password: String): Single[Boolean] = ???

        override def registerUser(request: RegisterUserRequest): Single[Boolean] = ???

        override def logoutUser(username: String): Single[Boolean] = ???
      }
      val user = User("niklegend", "nicola", "piscaglia", "bla", visible = true, new Date())
    }

  it should "register a new user" in {
    registerNewUser().subscribe(registered => assert(registered))
  }

  it should "authenticate a registered user" in {
    val user = fixture.user
    val dataStore = fixture.dataStore
    registerNewUser().subscribe(_ => {
      dataStore.loginUser(user.username, "password")
        .subscribe(logged => assert(logged))
    })
  }

  private def registerNewUser(): Single[Boolean] = {
    val user = fixture.user
    val dataStore = fixture.dataStore
    dataStore.registerUser(RegisterUserRequest(user.username, "password", user.firstName,
      user.lastName, user.bio, visible = true))
  }
}
