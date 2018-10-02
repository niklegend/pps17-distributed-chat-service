package it.unibo.dcs.service.user.impl

import it.unibo.dcs.service.user.{CreateUserRequest, User, UserDataStore, UserRepository}
import rx.lang.scala.Observable

final class UserRepositoryImpl(private[this] val userDataStore: UserDataStore) extends UserRepository {

  override def createUser(request: CreateUserRequest): Observable[User] = userDataStore.createUser(request)

  override def getUserByUsername(username: String): Observable[User] = userDataStore.getUserByUsername(username)

}
