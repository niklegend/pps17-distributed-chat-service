package it.unibo.dcs.service.room

package object request {

  final case class CreateUserRequest(username: String) extends AnyVal

  final case class GetRoomRequest(name: String)

  final case class CreateRoomRequest(name: String, username: String)

  final case class DeleteRoomRequest(name: String, username: String)

  final case class GetRoomsRequest(username: String)

  final case class JoinRoomRequest(name: String, username: String) extends JoinOrLeaveRoomRequest

  final case class LeaveRoomRequest(name: String, username: String) extends JoinOrLeaveRoomRequest

  sealed trait JoinOrLeaveRoomRequest {
    val name: String
    val username: String
  }
}
