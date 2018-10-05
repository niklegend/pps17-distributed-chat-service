package it.unibo.dcs.service.user

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.{AbstractVerticle, Context, Vertx}
import io.vertx.core.json.JsonObject
import io.vertx.scala.core.http.HttpServerResponse
import io.vertx.scala.ext.web.{Router, RoutingContext}
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.commons.service.ServiceVerticle
import it.unibo.dcs.service.user.interactor.{CreateUserUseCase, GetUserUseCase}
import it.unibo.dcs.service.user.model.User
import it.unibo.dcs.service.user.model.exception.UserNotFoundException
import it.unibo.dcs.service.user.repository.UserRepository
import it.unibo.dcs.service.user.request.{CreateUserRequest, GetUserRequest}
import rx.lang.scala.Subscriber

import UserVerticle.Implicits.httpResponseStatusToJsonObject

final class UserVerticle(private[this] val userRepository: UserRepository) extends ServiceVerticle {

  private var host: String = _
  private var port: Int = _

  private var getUserUseCase: GetUserUseCase = _
  private var createUserUseCase: CreateUserUseCase = _

  override def init(jVertx: Vertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(jVertx, context, verticle)
    host = config getString "host"
    port = config getInteger "port"

    val threadExecutor: ThreadExecutor = ???

    val postExecutionThread: PostExecutionThread = PostExecutionThread(RxHelper.scheduler(this.ctx))

    getUserUseCase = new GetUserUseCase(threadExecutor, postExecutionThread, userRepository)

    createUserUseCase = new CreateUserUseCase(threadExecutor, postExecutionThread, userRepository)
  }

  override def start(): Unit = {
    startHttpServer(host, port).subscribe()
  }

  override protected def initializeRouter(router: Router): Unit = {
    router.get("/users/:username").handler((routingContext: RoutingContext) => {
      val username = routingContext.request().getParam("username").get
      val subscriber: GetUserSubscriber = new GetUserSubscriber(routingContext.response())
      getUserUseCase(GetUserRequest(username)).subscribe(subscriber)
    })

    router.post("/users").handler((routingContext: RoutingContext) => {
      val user = routingContext.getBodyAsJson().get
      val subscriber: CreateUserSubscriber = new CreateUserSubscriber(routingContext.response())
      createUserUseCase(CreateUserRequest(user.getString("username"),
        user.getString("first_name"), user.getString("last_name"))).subscribe(subscriber)
    })
  }

  private final class GetUserSubscriber(private[this] val response: HttpServerResponse) extends Subscriber[User] {
    override def onNext(user: User): Unit = {
      val userJsonObject = JsonObject.mapFrom(user)
      response.write(userJsonObject.toString)
    }

    override def onCompleted(): Unit = response.end()

    override def onError(error: Throwable): Unit = error match {
      case UserNotFoundException(username) =>
        response.setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        response.write(setResponse(username, HttpResponseStatus.NOT_FOUND).toString)
    }
  }

  private final class CreateUserSubscriber(private[this] val response: HttpServerResponse) extends Subscriber[User] {
    override def onNext(user: User): Unit = {
      val userJsonObject = JsonObject.mapFrom(user)
      response.write(userJsonObject.toString)
    }

    override def onCompleted(): Unit = response.end()

    override def onError(error: Throwable): Unit = error match {
      case UserNotFoundException(username) =>
        response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
        response.write(setResponse(username, HttpResponseStatus.BAD_REQUEST).toString)
    }
  }

  private def setResponse(username: String, status: HttpResponseStatus): JsonObject = {
    val statusJson: JsonObject = status
    val extras = new JsonObject().put("username", username)
    new JsonObject().put("status", statusJson).put("extras", extras)
  }
}

object UserVerticle {
  object Implicits {
    implicit def httpResponseStatusToJsonObject(status: HttpResponseStatus): JsonObject = {
      new JsonObject().put("code", status.code()).put("reasonPhrase", status.reasonPhrase())
    }
  }
}
