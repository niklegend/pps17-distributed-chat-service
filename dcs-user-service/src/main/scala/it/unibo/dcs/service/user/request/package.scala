package it.unibo.dcs.service.user

import io.vertx.lang.scala.json.JsonObject
import it.unibo.dcs.service.user.model.User

import it.unibo.dcs.commons.dataaccess.Implicits._

package object request {

  final case class CreateUserRequest(username: String, firstName: String, lastName: String)

  final case class GetUserRequest(username: String) extends AnyVal

}
