package it.unibo.dcs.service.webapp.interaction

import com.google.gson.Gson
import io.vertx.lang.scala.json.{Json, JsonObject}
import it.unibo.dcs.service.webapp.model.{Room, User}

import scala.language.implicitConversions

/** It wraps all results produced by the use cases executions. */
object Results {

  /** Sum type that represents all the use cases results used in Distributed Chat Service application */
  sealed trait DcsResult

  final case class LoginResult(user: User, token: String) extends DcsResult

  final case class RegisterResult(user: User, token: String) extends DcsResult

  final case class RoomCreationResult(room: Room) extends DcsResult

  final case class RoomJoinResult(joinedUser: User) extends DcsResult

  /** It enables implicit conversions in order to clean code that deals with results. */
  object Implicits {

    private val gson = new Gson()

    implicit def registrationResultToJsonString(result: RegisterResult): String =
      resultToJsonString(result.user, _.put("token", result.token))

    implicit def loginResultToJsonString(result: LoginResult): String =
      resultToJsonString(result.user, _.put("token", result.token))

    implicit def roomCreationResultToJsonString(result: RoomCreationResult): String = resultToJsonString(result)

    implicit def roomJoinResultToJsonString(result: RoomJoinResult): String = resultToJsonString(result)

    private def resultToJsonString(result: Product, transformations: JsonObject => JsonObject = identity): String = {
      transformations(Json.fromObjectString(gson.toJson(result))) encode()
    }

  }

}
