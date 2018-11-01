package it.unibo.dcs.service.webapp.model

import java.util.Date

import com.google.gson.Gson
import io.vertx.lang.scala.json.{Json, JsonObject}

/** Model class that represents a user participation in a room
  *
  * @param joinDate date of the room join
  * @param room     the room joined by the user
  * @param user     the user who joined the room
  */
case class Participation(joinDate: Date, room: Room, user: User)

object Participation {

  object Implicits {

    private val gson = new Gson()

    implicit def participationToJsonObject(participation: Participation): JsonObject =
      Json.fromObjectString(gson.toJson(participation))
  }

}
