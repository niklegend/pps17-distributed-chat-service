package it.unibo.dcs.commons.service

import java.net.InetAddress

import io.vertx.core.json.JsonObject
import io.vertx.core.{AbstractVerticle, Context, Vertx, Future => VFuture}
import io.vertx.ext.web.client.{WebClient => JWebClient}
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.EventBus
import io.vertx.scala.core.http.{HttpServer, HttpServerOptions}
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.client.WebClient
import io.vertx.servicediscovery.types.HttpEndpoint
import io.vertx.servicediscovery.{Record, ServiceDiscovery}
import it.unibo.dcs.commons.VertxUtils
import it.unibo.dcs.commons.VertxUtils.Implicits._
import it.unibo.dcs.commons.service.ServiceVerticle._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

abstract class ServiceVerticle extends ScalaVerticle {

  private[this] var _eventBus: EventBus = _
  private[this] var _router: Router = _

  private[this] var discovery: ServiceDiscovery = _

  private[this] val records: ListBuffer[Record] = ListBuffer()

  override def init(jVertx: Vertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(jVertx, context, verticle)
    _eventBus = vertx.eventBus
    discovery = ServiceDiscovery.create(jVertx)

    _router = Router.router(vertx)
    initializeRouter(_router)
  }

  protected final def startHttpServer(host: String = DEFAULT_HOST,
                                      port: Int = DEFAULT_PORT,
                                      options: HttpServerOptions = DEFAULT_OPTIONS): Future[HttpServer] =
    vertx.createHttpServer(options)
      .requestHandler(_router accept _)
      .listenFuture(port, host)

  override def stop(): Unit = {
    records.foreach { record =>
      discovery.unpublish(record.getRegistration, _ => eventBus.publish(UNPUBLISH_CHANNEL, record))
    }
    records.clear()
    discovery.close()
  }

  protected def initializeRouter(router: Router): Unit

  protected final def eventBus: EventBus = _eventBus

  protected final def publishRecord(name: String,
                                    ssl: Boolean = DEFAULT_SSL,
                                    host: String = DEFAULT_HOST,
                                    port: Int = DEFAULT_PORT,
                                    root: String = DEFAULT_ROOT,
                                    metadata: JsonObject = DEFAULT_METADATA): Future[Record] = {
    VertxUtils.toFuture[Record] { handler =>
      val record = HttpEndpoint.createRecord(name, ssl, host, port, root, metadata)
      discovery.publish(record, { ar =>
        if (ar.succeeded) {
          val publishedRecord = ar.result
          records += publishedRecord
          eventBus.publish(PUBLISH_CHANNEL, publishedRecord)
        }
        handler(ar)
      })
    }
  }

  protected final def getWebClient(name: String): Future[WebClient] =
    getWebClientOrFail(name)
      .recoverWith {
        case _ => VertxUtils.toFuture[JWebClient, WebClient](WebClient(_)) { handler =>
          val consumer = eventBus.consumer[Record](PUBLISH_CHANNEL)
          consumer.handler { message =>
            val record = message.body
            if (record.getName == name && record.getType == RECORD_TYPE) {
              val webClient = discovery.getReference(record).getAs(classOf[JWebClient])
              handler(VFuture.succeededFuture(webClient))
              consumer.unregister()
            }
          }
        }
      }

  protected final def getWebClientOrFail(name: String): Future[WebClient] =
    VertxUtils.toFuture[JWebClient, WebClient](WebClient(_)) { handler =>
      HttpEndpoint.getWebClient(
        discovery,
        new JsonObject()
          .put("name", name)
          .put("type", RECORD_TYPE),
        handler)
    }

}

object ServiceVerticle {

  private val RECORD_TYPE = HttpEndpoint.TYPE

  private val DEFAULT_SSL = false
  private val DEFAULT_HOST = "0.0.0.0"
  private val DEFAULT_PORT = 80
  private val DEFAULT_ROOT = "/"

  private def DEFAULT_OPTIONS = HttpServerOptions()

  private def DEFAULT_METADATA = new JsonObject()

  private val CHANNEL_PREFIX: String = "discovery.records"
  protected val PUBLISH_CHANNEL: String = CHANNEL_PREFIX + ".published"
  protected val UNPUBLISH_CHANNEL: String = CHANNEL_PREFIX + ".unpublished"

}
