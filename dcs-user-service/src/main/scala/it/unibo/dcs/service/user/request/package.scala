package it.unibo.dcs.service.user

import it.unibo.dcs.commons.validation.ValidatorBuilder

package object request {

  final case class CreateUserRequest(username: String, firstName: String, lastName: String)

  final case class GetUserRequest(username: String) extends AnyVal

  object Validators {

    val createUserValidator = new ValidatorBuilder[CreateUserRequest]
      .addRule(request => checkNotEmpty(request.firstName))


    private def checkNotEmpty(field: String) = Option(field).isDefined && !field.isEmpty
  }

}
