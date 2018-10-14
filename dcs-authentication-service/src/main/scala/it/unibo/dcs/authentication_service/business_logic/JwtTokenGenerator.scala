package it.unibo.dcs.authentication_service.business_logic

import java.util.Date

import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTOptions}

class JwtTokenGenerator(jwtAuth: JWTAuth) {

  def generateToken(username: String): String = {
    jwtAuth.generateToken(Json.obj(("sub", username), ("iat", new Date().toInstant.toEpochMilli / 1000)),
      JWTOptions().setExpiresInMinutes(1440)) //expires after one day
  }

}

object JwtTokenGenerator{

  def apply(jwtAuth: JWTAuth): JwtTokenGenerator = new JwtTokenGenerator(jwtAuth)

}
