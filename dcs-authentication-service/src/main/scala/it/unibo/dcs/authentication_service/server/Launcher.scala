package it.unibo.dcs.authentication_service.server

import io.vertx.core.eventbus.{EventBus => JEventBus}
import io.vertx.scala.core.{Vertx, VertxOptions}
import io.vertx.scala.ext.jdbc.JDBCClient
import io.vertx.scala.ext.sql.SQLConnection
import io.vertx.servicediscovery.{Record, ServiceDiscovery}
import it.unibo.dcs.authentication_service.data.AuthenticationDataStoreDatabase
import it.unibo.dcs.authentication_service.repository.AuthenticationRepositoryImpl
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.commons.service.HttpEndpointPublisherImpl
import it.unibo.dcs.commons.service.codecs.RecordMessageCodec

object Launcher extends App {

  VertxHelper.toObservable[Vertx](Vertx.clusteredVertx(VertxOptions(), _)).flatMap { vertx =>
      val config = VertxHelper.readJsonObject("/db_config.json")
      VertxHelper.toObservable[SQLConnection](JDBCClient.createNonShared(vertx, config).getConnection(_))
        .map((vertx, _))
    }.subscribe({
      case (vertx, connection) =>
        val discovery = ServiceDiscovery.create(vertx)
        val eventBus = vertx.eventBus
        eventBus.asJava.asInstanceOf[JEventBus].registerDefaultCodec(classOf[Record], new RecordMessageCodec())
        val publisher = new HttpEndpointPublisherImpl(discovery, eventBus)
        val authDataStore = AuthenticationDataStoreDatabase(connection)
        val authRepository = AuthenticationRepositoryImpl(authDataStore)
        vertx.deployVerticle(AuthenticationVerticle(authRepository, publisher))
    }, cause => System.err.println(cause))
}