package it.unibo.dcs.service.user.impl

import io.vertx.scala.ext.sql.SQLClient
import it.unibo.dcs.service.user.{CreateUserRequest, User, UserDataStore}
import rx.lang.scala.Observable

class UserDataStoreDatabase(private[this] val client: SQLClient) extends UserDataStore {

  override def getUserByUsername(username: String): Observable[User] = ???

  override def createUser(user: CreateUserRequest): Observable[User] = ???

}
