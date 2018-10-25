package it.unibo.dcs.service.room

import io.vertx.core.{AbstractVerticle, Context, Vertx => JVertx}
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler
import it.unibo.dcs.commons.{RxHelper, VertxWebHelper}
import it.unibo.dcs.commons.VertxWebHelper.Implicits.contentTypeToString
import it.unibo.dcs.commons.interactor.ThreadExecutorExecutionContext
import it.unibo.dcs.commons.interactor.executor.PostExecutionThread
import it.unibo.dcs.commons.service.{HttpEndpointPublisher, ServiceVerticle}
import it.unibo.dcs.service.room.RoomVerticle.Implicits._
import it.unibo.dcs.service.room.interactor.usecases._
import it.unibo.dcs.service.room.interactor.validations._
import it.unibo.dcs.service.room.repository.RoomRepository
import it.unibo.dcs.service.room.request.{CreateRoomRequest, CreateUserRequest, DeleteRoomRequest, GetRoomsRequest}
import it.unibo.dcs.service.room.subscriber._
import it.unibo.dcs.service.room.validator._
import org.apache.http.entity.ContentType

import scala.language.implicitConversions

final class RoomVerticle(private[this] val roomRepository: RoomRepository, val publisher: HttpEndpointPublisher) extends ServiceVerticle {

  private[this] var host: String = _
  private[this] var port: Int = _

  override def init(jVertx: JVertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(jVertx, context, verticle)

    host = config.getString("host")
    port = config.getInteger("port")
  }

  override protected def initializeRouter(router: Router): Unit = {
    router.route().handler(BodyHandler.create())
    VertxWebHelper.setupCors(router)

    val threadExecutor = ThreadExecutorExecutionContext(vertx)
    val postExecutionThread = PostExecutionThread(RxHelper.scheduler(this.ctx))

    val createUserUseCase = new CreateUserUseCase(threadExecutor, postExecutionThread, roomRepository)
    val createRoomUseCase = new CreateRoomUseCase(threadExecutor, postExecutionThread, roomRepository)
    val deleteRoomUseCase = new DeleteRoomUseCase(threadExecutor, postExecutionThread, roomRepository)
    val getRoomsUseCase = new GetRoomsUseCase(threadExecutor, postExecutionThread, roomRepository)

    val deleteRoomValidator = DeleteRoomValidator()
    val createRoomValidator = CreateRoomValidator()
    val createUserValidator = CreateUserValidator()
    val getRoomsValidator = GetRoomsValidator()

    val deleteRoomValidation = new DeleteRoomValidation(threadExecutor, postExecutionThread, deleteRoomValidator)
    val createRoomValidation = new CreateRoomValidation(threadExecutor, postExecutionThread, createRoomValidator)
    val getRoomsValidation = new GetRoomsValidation(threadExecutor, postExecutionThread, getRoomsValidator)
    val createUserValidation = new CreateUserValidation(threadExecutor, postExecutionThread, createUserValidator)

    router.post("/createUser")
      .consumes(ContentType.APPLICATION_JSON)
      .consumes(ContentType.APPLICATION_JSON)
      .handler(routingContext => {
        val request = routingContext.getBodyAsJson.head
        val checkSubscriber = new CreateUserValiditySubscriber(routingContext.response(), request, createUserUseCase)
        createUserValidation(request, checkSubscriber)
      })

    router.post("/createRoom")
      .consumes(ContentType.APPLICATION_JSON)
      .consumes(ContentType.APPLICATION_JSON)
      .handler(routingContext => {
        val request = routingContext.getBodyAsJson.head
        val checkSubscriber = new CreateRoomValiditySubscriber(routingContext.response(), request, createRoomUseCase)
        createRoomValidation(request, checkSubscriber)
      })

    router.post("/deleteRoom")
      .consumes(ContentType.APPLICATION_JSON)
      .consumes(ContentType.APPLICATION_JSON)
      .handler(routingContext => {
        val request = routingContext.getBodyAsJson.head
        val checkSubscriber = new DeleteRoomValiditySubscriber(routingContext.response(), request, deleteRoomUseCase)
        deleteRoomValidation(request, checkSubscriber)
      })

    router.get("/rooms")
      .consumes(ContentType.APPLICATION_JSON)
      .handler(routingContext => {
        val request = GetRoomsRequest()
        val checkSubscriber = new GetRoomsValiditySubscriber(routingContext.response(), request, getRoomsUseCase)
        getRoomsValidation(request, checkSubscriber)
      })
  }

  override def start(): Unit = startHttpServer(host, port)
    .doOnCompleted(
      publisher.publish(name = "room-service", host = host, port = port)
        .subscribe(record => log.info(s"${record.getName} record published!"),
          log.error(s"Could not publish record", _)))
    .subscribe(server => log.info(s"Server started at http://$host:${server.actualPort}"),
      log.error(s"Could not start server at http://$host:$port", _))

}

object RoomVerticle {

  object Implicits {

    private val usernameJsonKey = "username"
    private val roomJsonKey = "name"

    implicit def jsonObjectToCreateUserRequest(json: JsonObject): CreateUserRequest =
      CreateUserRequest(json.getString(usernameJsonKey))

    implicit def jsonObjectToCreateRoomRequest(json: JsonObject): CreateRoomRequest =
      CreateRoomRequest(json.getString(roomJsonKey), json.getString(usernameJsonKey))

    implicit def jsonObjectToDeleteRoomRequest(json: JsonObject): DeleteRoomRequest =
      DeleteRoomRequest(json.getString(roomJsonKey), json.getString(usernameJsonKey))
  }

}
