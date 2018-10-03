package it.unibo.dcs.authentication_service.common

import rx.lang.scala.Observable

trait AuthenticationRepository {

  def createUser(username: String, password: String): Observable[String]

  def loginUser(username: String, password: String): Observable[String]

  def logoutUser(username: String): Observable[Unit]

}
