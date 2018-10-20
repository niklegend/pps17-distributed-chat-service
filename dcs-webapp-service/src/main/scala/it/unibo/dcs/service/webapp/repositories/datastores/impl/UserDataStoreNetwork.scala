package it.unibo.dcs.service.webapp.repositories.datastores.impl

import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.UserApi
import rx.lang.scala.Observable

class UserDataStoreNetwork(private val userApi: UserApi) extends UserDataStore {

  override def getUserByUsername(username: String): Observable[User] = userApi.getUserByUsername(username)

  override def createUser(request: RegisterUserRequest, token: String): Observable[User] =
    userApi.createUser(request, token)

}
