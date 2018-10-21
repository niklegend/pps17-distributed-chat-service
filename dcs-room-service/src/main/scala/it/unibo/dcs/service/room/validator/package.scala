package it.unibo.dcs.service.room

import it.unibo.dcs.commons.validation.Validator
import it.unibo.dcs.exceptions.{RoomNameRequiredException, UsernameRequiredException}
import it.unibo.dcs.service.room.request.{CreateRoomRequest, CreateUserRequest, DeleteRoomRequest}
import it.unibo.dcs.service.room.validator.Messages._

package object validator {

  object CreateUserValidator {
    def apply: Validator[CreateUserRequest] = Validator[CreateUserRequest] {
      builder =>
        builder.addRule(request =>
          builder.Conditions.stringNotEmpty(request.username),
          UsernameRequiredException)
    }
  }

  object CreateRoomValidator {
    def apply: Validator[CreateRoomRequest] = Validator[CreateRoomRequest] {
      builder =>
        builder
          .addRule(request =>
            builder.Conditions.stringNotEmpty(request.name),
            RoomNameRequiredException)

          .addRule(request =>
            builder.Conditions.stringNotEmpty(request.username),
            UsernameRequiredException)
    }
  }

  object DeleteRoomValidator {
    def apply: Validator[DeleteRoomRequest] = Validator[DeleteRoomRequest] {
      builder =>
        builder
          .addRule(request =>
            builder.Conditions.stringNotEmpty(request.name),
            RoomNameRequiredException
          )
          .addRule(request =>
            builder.Conditions.stringNotEmpty(request.username),
            UsernameRequiredException
          )
    }
  }

  object Messages {
    val missingUsernameInRegistration = "Username missing in user creation request"
    val missingRoomNameInCreation = "Room name missing in room creation request"
    val missingCreatorInCreation = "Creator username missing in room creation request"
    val missingCreatorInDeletion = "Creator username missing in room deletion request"
    val missingRoomNameInDeletion = "Room name missing in room deletion request"
  }

}
