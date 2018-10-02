package repositories.datastores

import java.util.Date

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import org.scalatest.FlatSpec
import rx.lang.scala.Observable

class UserDataStoreSpec extends FlatSpec{

  def fixture =
    new {
      val dataStore: UserDataStore = new UserDataStore {
        override def getUserByUsername(username: String): Observable[User] = ???

        override def createUser(request: RegisterUserRequest): Observable[User] = ???
      }
      val user = User("niklegend", "nicola", "piscaglia", "bla", visible = true, new Date())
    }
}
