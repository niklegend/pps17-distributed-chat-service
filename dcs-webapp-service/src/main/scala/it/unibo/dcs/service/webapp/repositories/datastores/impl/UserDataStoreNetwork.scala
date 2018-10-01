package it.unibo.dcs.service.webapp.repositories.datastores.impl

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import rx.lang.scala.Observable

class UserDataStoreNetwork extends UserDataStore {
  override def getUserByUsername(username: String): Observable[User] = ???

  override def createUser(request: Requests.RegisterUserRequest): Observable[User] = ???
}
