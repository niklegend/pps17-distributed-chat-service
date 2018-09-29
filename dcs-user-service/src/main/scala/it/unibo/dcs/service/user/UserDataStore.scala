package it.unibo.dcs.service.user

import rx.lang.scala.Observable

trait UserDataStore {

  def getUserByUsername(username: String): Observable[User]

  def createUser(request: CreateUserRequest): Observable[User]

}
