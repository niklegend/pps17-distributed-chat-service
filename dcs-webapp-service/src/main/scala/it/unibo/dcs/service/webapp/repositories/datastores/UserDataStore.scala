package it.unibo.dcs.service.webapp.repositories.datastores


import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.lang.scala.Observable

trait UserDataStore {
  @throws(classOf[IllegalArgumentException])
  def getUserByUsername(username: String): Observable[User]

  @throws(classOf[IllegalArgumentException])
  def createUser(request: RegisterUserRequest): Observable[User]
}
