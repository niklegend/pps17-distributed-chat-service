package it.unibo.dcs.authentication_service

import it.unibo.dcs.authentication_service.repository.AuthenticationRepository
import it.unibo.dcs.authentication_service.request.{CheckTokenRequest, LoginUserRequest, LogoutUserRequest, RegisterUserRequest}
import it.unibo.dcs.commons.validation.Validator
import it.unibo.dcs.exceptions.{PasswordRequiredException, TokenRequiredException, UserNotFoundException, UsernameRequiredException}

package object validator {

  object LogoutUserValidator {
    def apply(authRepository: AuthenticationRepository): Validator[LogoutUserRequest] = Validator[LogoutUserRequest] {
      builder =>
        builder.addRule(request =>
          builder.Conditions.stringNotEmpty(request.token), TokenRequiredException)
    }
  }

  object RegistrationValidator {
    def apply: Validator[RegisterUserRequest] = Validator[RegisterUserRequest] {
      builder =>
        builder
          .addRule(
            request => builder.Conditions.stringNotEmpty(request.username),
            UsernameRequiredException)
          .addRule(
            request => request.password != null && !request.password.isEmpty,
            PasswordRequiredException)
    }
  }

  object LoginValidator {
    def apply(authRepository: AuthenticationRepository): Validator[LoginUserRequest] = Validator[LoginUserRequest] {
      builder =>
        builder
          .addRule(
            request => builder.Conditions.stringNotEmpty(request.username),
            UsernameRequiredException
          )

          .addRule(
            request => builder.Conditions.stringNotEmpty(request.password),
            PasswordRequiredException
          )

          .addRule(request => authRepository.checkUserExistence(request.username)
            .doOnError(throw UserNotFoundException(request.username)))
    }
  }

  object CheckTokenValidator {
    def apply: Validator[CheckTokenRequest] = Validator[CheckTokenRequest] {
      builder =>
        builder
          .addRule(
            request => builder.Conditions.stringNotEmpty(request.token), TokenRequiredException)
    }
  }

}


