package it.unibo.dcs.service.webapp.repositories.datastores.impl

<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.UserApi
import rx.lang.scala.Observable

class UserDataStoreNetwork(private val userApi: UserApi) extends UserDataStore {
  override def getUserByUsername(username: String): Observable[User] = userApi.getUserByUsername(username)

  override def createUser(request: Requests.RegisterUserRequest): Observable[User] = userApi.createUser(request)
=======
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.datastores.UserDataStore
import rx.lang.scala.Observable

class UserDataStoreNetwork extends UserDataStore {
  override def getUserByUsername(username: String): Observable[User] = ???

  override def createUser(request: Requests.RegisterUserRequest): Observable[User] = ???
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
