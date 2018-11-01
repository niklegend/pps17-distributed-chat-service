package it.unibo.dcs.service.authentication.interactor.usecases

import io.vertx.scala.ext.auth.jwt.JWTAuth
import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.authentication.business_logic.JwtTokenGenerator
import it.unibo.dcs.service.authentication.repository.AuthenticationRepository
import it.unibo.dcs.service.authentication.request.Requests.TokenRequest
import rx.lang.scala.Observable

/** It represents any use case that should return a jwt token
  *
  * @param threadExecutor      thread executor that will perform the subscription
  * @param postExecutionThread thread that will be notified of the subscription result
  * @param authRepository      authentication repository reference
  * @param jwtAuth             jwt authentication provider
  * @usecase creation of a jwt token */
trait ReturningTokenUseCase {

  /** Method that maps the unit result to a new token
    *
    * @param observable the observable to map
    * @param username the provided username
    * */
  def createToken(username: String, jwtAuth: JWTAuth): String =
    JwtTokenGenerator(jwtAuth).generateToken(username)

}