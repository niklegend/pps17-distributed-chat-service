package it.unibo.dcs

/** It contains all the exceptions used by the APIs */
package object exceptions {

  /** Sum type representing all the specific exceptions for Distributed Chat Service application */
  sealed trait DcsException extends RuntimeException

  final case class UsernameAlreadyTaken(username: String) extends DcsException

  final case class MissingUsernameException(message: String) extends DcsException

  final case class MissingLastNameException(message: String) extends DcsException

  final case class MissingFirstNameException(message: String) extends DcsException

  final case class RegistrationResponseException(message: String) extends DcsException

  final case class LoginResponseException(message: String) extends DcsException

  final case class GetUserResponseException(message: String) extends DcsException

  final case class UserCreationResponseException(message: String) extends DcsException

  final case class RoomDeletionResponseException(message: String) extends DcsException

  final case class RoomCreationResponseException(message: String) extends DcsException

  final case class RegistrationValidityResponseException(message: String) extends DcsException

  final case class LogoutValidityResponseException(message: String) extends DcsException

  final case class InvalidTokenException(message: String) extends DcsException

}

