package it.unibo.dcs.authentication_service.common

trait TokenRequest {

  def username: String
  def password: String

}
