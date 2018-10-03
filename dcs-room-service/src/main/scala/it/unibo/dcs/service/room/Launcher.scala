package it.unibo.dcs.service.room

import io.vertx.core.eventbus.{EventBus => JEventBus}
import io.vertx.scala.core.{Vertx, VertxOptions}
import io.vertx.servicediscovery.{Record, ServiceDiscovery}
import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.commons.service.HttpEndpointPublisherImpl
import it.unibo.dcs.commons.service.codecs.RecordMessageCodec
import it.unibo.dcs.service.room.data.RoomDataStore
import it.unibo.dcs.service.room.repository.impl.RoomRepositoryImpl

import scala.util.{Failure, Success}

object Launcher extends App {

  Vertx.clusteredVertx(VertxOptions(), _.toTry match {
    case Success(vertx) =>
      val discovery = ServiceDiscovery.create(vertx)
      val eventBus = vertx.eventBus
      eventBus.asJava.asInstanceOf[JEventBus].registerDefaultCodec(classOf[Record], new RecordMessageCodec())
      val publisher = new HttpEndpointPublisherImpl(discovery, eventBus)
      val roomDataStore: RoomDataStore = ???
      val roomRepository = new RoomRepositoryImpl(roomDataStore)
      vertx.deployVerticle(new RoomVerticle(roomRepository, publisher))
    case Failure(cause) =>
  })

}
