package it.unibo.dcs.service.webapp.usecases

import it.unibo.dcs.service.webapp.model.User

object Results {

  final case class LoginResult(loggedUser: User, token: String)

  final case class LogoutResult()

  final case class RegisterResult(registeredUser: User, token: String)

}
