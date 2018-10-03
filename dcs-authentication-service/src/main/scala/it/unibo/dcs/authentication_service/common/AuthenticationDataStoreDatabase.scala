package it.unibo.dcs.authentication_service.common

import rx.lang.scala.Observable

class AuthenticationDataStoreDatabase extends AuthenticationDataStore {

  override def createUser(username: String, password: String): Observable[String] = ???

  override def loginUser(username: String, password: String): Observable[String] = ???

  override def logoutUser(username: String): Observable[Unit] = ???

}
