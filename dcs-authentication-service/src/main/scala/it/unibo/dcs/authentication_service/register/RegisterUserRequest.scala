package it.unibo.dcs.authentication_service.register

import it.unibo.dcs.authentication_service.common.TokenRequest

final case class RegisterUserRequest(username: String, password: String) extends TokenRequest