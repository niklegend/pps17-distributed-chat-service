package it.unibo.dcs

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.{Json, JsonArray, JsonObject}
import io.vertx.scala.ext.web.client.HttpResponse
import it.unibo.dcs.exceptions.ErrorType._

import exceptions.Implicits.jsonObjectToDcsException

/** It contains all the exceptions used by the APIs */
package object exceptions {

  private val KEY_ERROR = "error"

  private val KEY_TYPE = "type"
  private val KEY_EXTRAS = "extras"

  private val KEY_DESCRIPTION = "description"
  private val KEY_NAME = "name"
  private val KEY_USERNAME = "username"

  object ErrorType {

    val Internal = "Internal"

    val UserAlreadyExists = "USER_ALREADY_EXISTS"

    val UserNotFound = "USER_NOT_FOUND"

    val RoomAlreadyExists = "ROOM_ALREADY_EXISTS"

    val RoomNotFound = "ROOM_NOT_FOUND"

    val WrongUsernameOrPassword = "WRONG_USERNAME_OR_PASSWORD"

    val InvalidToken = "INVALID_TOKEN"

    val UsernameRequired = "USERNAME_REQUIRED"

    val FirstNameRequired = "FIRST_NAME_REQUIRED"

    val LastNameRequired = "LAST_NAME_REQUIRED"

    val PasswordRequired = "PASSWORD_REQUIRED"

    val PasswordConfirmRequired = "PASSWORD_CONFIRM_REQUIRED"

    val TokenRequired = "TOKEN_REQUIRED"

    val RoomNameRequired = "ROOM_NAME_REQUIRED"

  }

  /** Sum type representing all the specific exceptions for Distributed Chat Service application */
  sealed abstract class DcsException(val errorType: String) extends RuntimeException

  final case class InternalException(description: String = "") extends DcsException(Internal)

  final case class UserAlreadyExistsException(username: String) extends DcsException(UserAlreadyExists)

  final case class UserNotFoundException(username: String) extends DcsException(UserNotFound)

  final case class RoomAlreadyExistsException(name: String) extends DcsException(RoomAlreadyExists)

  final case class RoomNotFoundException(name: String) extends DcsException(RoomNotFound)

  final case object InvalidTokenException extends DcsException(InvalidToken)

  final case object WrongUsernameOrPasswordException extends DcsException(WrongUsernameOrPassword)

  /* Field required */
  final case object UsernameRequiredException extends DcsException(UsernameRequired)

  final case object FirstNameRequiredException extends DcsException(FirstNameRequired)

  final case object LastNameRequiredException extends DcsException(LastNameRequired)

  final case object PasswordRequiredException extends DcsException(PasswordRequired)

  final case object PasswordConfirmRequiredException extends DcsException(PasswordRequired)

  final case object TokenRequiredException extends DcsException(TokenRequired)

  final case object RoomNameRequiredException extends DcsException(RoomNameRequired)

  object Implicits {

    implicit def throwableToJsonObject(cause: Throwable): JsonObject = {
      val error = new JsonObject()
      cause match {
        case e: DcsException =>
          error.put(KEY_TYPE, e.errorType)
          e match {
            case UserAlreadyExistsException(username) =>
              error.put(KEY_EXTRAS, Json.obj((KEY_USERNAME, username)))
            case UserNotFoundException(username) =>
              error.put(KEY_EXTRAS, Json.obj((KEY_USERNAME, username)))
            case RoomAlreadyExistsException(name) =>
              error.put(KEY_EXTRAS, Json.obj((KEY_NAME, name)))
            case RoomNotFoundException(name) =>
              error.put(KEY_EXTRAS, Json.obj((KEY_NAME, name)))
            case InternalException(description) =>
              if (description != null && description.nonEmpty)
                error.put(KEY_EXTRAS, Json.obj((KEY_DESCRIPTION, description)))
          }
        case _ =>
          error.put(KEY_TYPE, Internal)

          val description = cause.getMessage
          if (description != null && description.nonEmpty)
            error.put(KEY_EXTRAS, Json.obj((KEY_DESCRIPTION, description)))
      }
      Json.obj((KEY_ERROR, error))
    }

    implicit def jsonObjectToDcsException(json: JsonObject): JsonObject = {
      if (json.containsKey(KEY_ERROR)) {
        if (json.containsKey(KEY_TYPE)) {
          json.getString(KEY_TYPE) match {
            case UserNotFound =>
              if (json.containsKey(KEY_EXTRAS)) {
                val extras = json.getJsonObject(KEY_EXTRAS)
                if (extras.containsKey(KEY_USERNAME)) {
                  val username = extras.getString(KEY_USERNAME)
                  throw UserNotFoundException(username)
                }
                throw InternalException(s"Missing key '$KEY_USERNAME' in $UserNotFound error")
              }
              throw InternalException(s"Missing key '$KEY_EXTRAS' in $UserNotFound error")
            case UserAlreadyExists =>
              if (json.containsKey(KEY_EXTRAS)) {
                val extras = json.getJsonObject(KEY_EXTRAS)
                if (extras.containsKey(KEY_USERNAME)) {
                  val username = extras.getString(KEY_USERNAME)
                  throw UserAlreadyExistsException(username)
                }
                throw InternalException(s"Missing key '$KEY_USERNAME' in $UserAlreadyExists error")
              }
              throw InternalException(s"Missing key '$KEY_EXTRAS' in $UserAlreadyExists error")
            case RoomNotFound =>
              if (json.containsKey(KEY_EXTRAS)) {
                val extras = json.getJsonObject(KEY_EXTRAS)
                if (extras.containsKey(KEY_NAME)) {
                  val name = extras.getString(KEY_NAME)
                  throw RoomNotFoundException(name)
                }
                throw InternalException(s"Missing key '$KEY_NAME' in $RoomNotFound error")
              }
              throw InternalException(s"Missing key '$KEY_EXTRAS' in $RoomNotFound error")
            case RoomAlreadyExists =>
              if (json.containsKey(KEY_EXTRAS)) {
                val extras = json.getJsonObject(KEY_EXTRAS)
                if (extras.containsKey(KEY_NAME)) {
                  val name = extras.getString(KEY_NAME)
                  throw RoomAlreadyExistsException(name)
                }
                throw InternalException(s"Missing key '$KEY_NAME' in $RoomAlreadyExists error")
              }
              throw InternalException(s"Missing key '$KEY_EXTRAS' in $RoomAlreadyExists error")
            case WrongUsernameOrPassword =>
              throw WrongUsernameOrPasswordException
            case UsernameRequired =>
              throw UsernameRequiredException
            case FirstNameRequired =>
              throw FirstNameRequiredException
            case LastNameRequired =>
              throw LastNameRequiredException
            case PasswordRequired =>
              throw PasswordRequiredException
            case PasswordConfirmRequired =>
              throw PasswordConfirmRequiredException
            case TokenRequired =>
              throw TokenRequiredException
            case RoomNameRequired =>
              throw RoomNameRequiredException
            case Internal =>
              if (json.containsKey(KEY_EXTRAS)) {
                val extras = json.getJsonObject(KEY_EXTRAS)
                if (extras.containsKey(KEY_DESCRIPTION)) {
                  val description = extras.getString(KEY_DESCRIPTION)
                  throw InternalException(description)
                }
              }
              throw InternalException()
          }
          throw InternalException(s"Missing key '$KEY_TYPE' in DCS error")
        }
      }
      json
    }

    implicit def throwableToHttpResponseStatus(cause: Throwable): HttpResponseStatus = cause match {
      case UserNotFoundException(_)
           | RoomNotFoundException(_) =>
        HttpResponseStatus.NOT_FOUND
      case UserAlreadyExistsException(_)
           | RoomAlreadyExistsException(_) =>
        HttpResponseStatus.CONFLICT
      case UsernameRequiredException
           | FirstNameRequiredException
           | LastNameRequiredException
           | PasswordRequiredException
           | PasswordConfirmRequiredException
           | TokenRequiredException
           | RoomNameRequiredException =>
        HttpResponseStatus.PRECONDITION_FAILED
      case InvalidTokenException
           | WrongUsernameOrPasswordException =>
        HttpResponseStatus.UNAUTHORIZED
      case _ => HttpResponseStatus.INTERNAL_SERVER_ERROR
    }
  }

  def bodyAsJsonObject[T](default: => JsonObject = Json.emptyObj()): HttpResponse[T] => JsonObject = response =>
    jsonObjectToDcsException(response.bodyAsJsonObject().getOrElse(default))

  def bodyAsJsonArray[T](default: => JsonArray = Json.emptyArr()): HttpResponse[T] => JsonArray = response => response.bodyAsJsonArray()
    .getOrElse(default)

}
