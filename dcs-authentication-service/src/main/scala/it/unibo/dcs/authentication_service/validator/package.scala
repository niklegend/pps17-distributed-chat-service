package it.unibo.dcs.authentication_service

import it.unibo.dcs.authentication_service.repository.AuthenticationRepository
import it.unibo.dcs.authentication_service.request.LogoutUserRequest
import it.unibo.dcs.commons.validation.Validator
import it.unibo.dcs.exceptions.InvalidTokenException

package object validator {

  object LogoutUserValidator {
    def apply(authRepository: AuthenticationRepository): Validator[LogoutUserRequest] = Validator[LogoutUserRequest] {
      builder =>
        /* User to logout doesn't exist in Auth Data Store*/
        builder.addRule(request =>
          authRepository.isTokenValid(request.token)
            .singleOption
            .map(opt => opt.fold()(result =>
              if (!result) throw InvalidTokenException("Invalid token passed with logout request"))))
    }

  }

}
