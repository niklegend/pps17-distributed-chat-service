package it.unibo.dcs.service.webapp.repositories

object Requests {

  final case class LoginUserRequest(username: String, password: String)

  final case class RegisterUserRequest(username: String, password: String,
                                       firstName: String, lastName: String,
                                       bio: String, visible: String)

}
