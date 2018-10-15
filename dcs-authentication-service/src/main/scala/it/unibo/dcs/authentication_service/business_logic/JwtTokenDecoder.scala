package it.unibo.dcs.authentication_service.business_logic

import io.vertx.lang.scala.json.Json
import org.apache.commons.codec.binary.Base64

class JwtTokenDecoder() {

  private val base64 = new Base64(true)

  def getUsernameFromToken(token: String): String = {
    val base64EncodedBody = token.split("\\.")(1)
    val body = new String(base64.decode(base64EncodedBody))
    Json.fromObjectString(body).getString("sub")
  }

}

object JwtTokenDecoder{

  def apply(): JwtTokenDecoder = new JwtTokenDecoder()

}
