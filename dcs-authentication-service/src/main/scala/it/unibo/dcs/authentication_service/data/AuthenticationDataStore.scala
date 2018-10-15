package it.unibo.dcs.authentication_service.data

import java.util.Date

import rx.lang.scala.Observable

trait AuthenticationDataStore {

  def createUser(username: String, password: String): Observable[Unit]

  def checkUserExistence(username: String): Observable[Unit]

  def checkUserCredentials(username: String, password: String) : Observable[Unit]

  def invalidToken(token: String, expirationDate: Date): Observable[Unit]

  def isTokenValid(token: String): Observable[Boolean]

}
