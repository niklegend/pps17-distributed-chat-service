package it.unibo.dcs.service.webapp.repositories

import it.unibo.dcs.service.webapp.model.User
<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import it.unibo.dcs.service.webapp.repositories.impl.UserRepositoryImpl
=======
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
import rx.lang.scala.Observable

trait UserRepository {
  def getUserByUsername(username: String): Observable[User]

  def registerUser(request: RegisterUserRequest): Observable[User]
}
<<<<<<< HEAD

object UserRepository {
  def apply(userDataStore: UserDataStore): UserRepository = new UserRepositoryImpl(userDataStore)
}
=======
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
