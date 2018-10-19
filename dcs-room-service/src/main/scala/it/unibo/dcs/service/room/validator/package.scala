package it.unibo.dcs.service.room

import it.unibo.dcs.commons.validation.Validator
import it.unibo.dcs.exceptions.{MissingRoomNameException, MissingUsernameException}
import it.unibo.dcs.service.room.request.{CreateRoomRequest, CreateUserRequest, DeleteRoomRequest}

package object validator {

  object CreateUserValidator {
    def apply: Validator[CreateUserRequest] = Validator[CreateUserRequest] {
      builder =>
        builder.addRule(builder.observableRule(request =>
          builder.Conditions.stringNotEmpty(request.username),
          MissingUsernameException("Username missing in user creation request")))
    }
  }

  object CreateRoomValidator {
    def apply: Validator[CreateRoomRequest] = Validator[CreateRoomRequest] {
      builder =>
        builder
          .addRule(builder.observableRule(request =>
            builder.Conditions.stringNotEmpty(request.name),
            MissingRoomNameException("Room name missing in room creation request")))

          .addRule(builder.observableRule(request =>
            builder.Conditions.stringNotEmpty(request.username),
            MissingUsernameException("Creator username missing in room creation request")))
    }
  }

  object DeleteRoomValidator {
    def apply: Validator[DeleteRoomRequest] = Validator[DeleteRoomRequest] {
      builder =>
        builder
          .addRule(builder.observableRule(request =>
            builder.Conditions.stringNotEmpty(request.name),
            MissingRoomNameException("Room name missing in room deletion request")))

          .addRule(builder.observableRule(request =>
            builder.Conditions.stringNotEmpty(request.username),
            MissingUsernameException("Creator username missing in room deletion request")))
    }
  }

}
