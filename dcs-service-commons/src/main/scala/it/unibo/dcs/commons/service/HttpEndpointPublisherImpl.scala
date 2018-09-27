package it.unibo.dcs.commons.service

import io.vertx.core.json.JsonObject
import io.vertx.core.{Future => VFuture}
import io.vertx.scala.core.eventbus.EventBus
import io.vertx.servicediscovery.types.HttpEndpoint
import io.vertx.servicediscovery.{Record, ServiceDiscovery}
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.service.Constants.{PUBLISH_CHANNEL, UNPUBLISH_CHANNEL}

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Future.unit

class HttpEndpointPublisherImpl(private[this] val discovery: ServiceDiscovery, private[this] val eventBus: EventBus) extends HttpEndpointPublisher {

  private[this] val records: ListBuffer[Record] = ListBuffer()

  override def publish(name: String, ssl: Boolean, host: String, port: Int, root: String, metadata: JsonObject): Future[Record] =
    VertxHelper.toFuture[Record] { handler =>
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

  override def unpublish(record: Record): Future[Record] =
    VertxHelper.toFuture[Record] { handler =>
      discovery.unpublish(record.getRegistration, {
        _ => eventBus.publish(UNPUBLISH_CHANNEL, record)
          records -= record
          handler(VFuture.succeededFuture(record))
      })
    }

  override def clear()(implicit executor: ExecutionContext): Future[Unit] = {
    Future.sequence(records.map(unpublish)).map { _ => }
  }

}
