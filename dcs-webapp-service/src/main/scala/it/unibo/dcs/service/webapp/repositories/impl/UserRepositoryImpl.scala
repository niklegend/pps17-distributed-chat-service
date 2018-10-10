package it.unibo.dcs.service.webapp.repositories.impl

import it.unibo.dcs.service.webapp.model.User
<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.UserRepository
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import rx.lang.scala.Observable

class UserRepositoryImpl(userDataStore: UserDataStore) extends UserRepository {
  override def getUserByUsername(username: String): Observable[User] = userDataStore.getUserByUsername(username)

  override def registerUser(request: RegisterUserRequest): Observable[User] = userDataStore.createUser(request)
=======
import it.unibo.dcs.service.webapp.repositories.{Requests, UserRepository}
import rx.lang.scala.Observable

class UserRepositoryImpl extends UserRepository {
  override def getUserByUsername(username: String): Observable[User] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Observable[User] = ???
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
