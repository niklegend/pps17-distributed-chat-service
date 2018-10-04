package it.unibo.dcs.authentication_service.common

import java.time.LocalDateTime

import rx.lang.scala.Observable

class AuthenticationRepositoryImpl(private[this] val authDataStore: AuthenticationDataStore)
  extends AuthenticationRepository {

  override def createUser(username: String, password: String): Observable[Unit] =
    authDataStore.createUser(username, password)

  override def loginUser(username: String, password: String): Observable[Unit] =
    authDataStore.checkUserExistence(username, password)

  override def logoutUser(token: String, tokenExpirationDate: LocalDateTime): Observable[Unit] =
    authDataStore.invalidToken(token, tokenExpirationDate)

}
