package it.unibo.dcs.service.user

import rx.lang.scala.Observable

final case class CreateUserRequest(username: String, firstName: String, lastName: String, bio: String)

trait UserRepository {

  def createUser(request: CreateUserRequest): Observable[User]

  def getUserByUsername(username: String): Observable[User]

}
