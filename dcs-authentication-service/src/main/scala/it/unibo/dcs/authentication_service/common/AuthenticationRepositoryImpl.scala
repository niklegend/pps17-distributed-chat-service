package it.unibo.dcs.authentication_service.common

import rx.lang.scala.Observable

class AuthenticationRepositoryImpl(private[this] val authDataStore: AuthenticationDataStore)
  extends AuthenticationRepository {

  override def createUser(username: String, password: String): Observable[String] =
    authDataStore.createUser(username, password)

  override def loginUser(username: String, password: String): Observable[String] =
    authDataStore.loginUser(username, password)

  override def logoutUser(username: String): Observable[Unit] = authDataStore.logoutUser(username)

}
