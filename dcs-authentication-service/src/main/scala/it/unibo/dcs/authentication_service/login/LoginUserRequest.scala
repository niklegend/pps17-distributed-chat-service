package it.unibo.dcs.authentication_service.login

import it.unibo.dcs.authentication_service.common.TokenRequest

final case class LoginUserRequest(username: String, password: String) extends TokenRequest