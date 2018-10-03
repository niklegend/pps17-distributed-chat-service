package it.unibo.dcs.authentication_service.common

import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.auth.jwt.JWTAuth

class JwtTokenGenerator(jwtAuth: JWTAuth) {

  def generate(username: String): String ={
    jwtAuth.generateToken(Json.obj(("sub", username), ("iat", System.currentTimeMillis())))
  }

}
