package it.unibo.dcs.service.room

package object request {

  final case class CreateUserRequest(username: String) extends AnyVal

  final case class DeleteRoomRequest(name: String, username: String)

}
