package it.unibo.dcs.commons.service.codecs

import io.vertx.lang.scala.json.JsonObject
import io.vertx.servicediscovery.Record

final class RecordMessageCodec extends AbstractIdentityMessageCodec[Record] {

  protected override def encodeToWire(record: Record): JsonObject = record.toJson

  protected override def decodeFromWire(jsonObject: JsonObject): Record = new Record(jsonObject)

  override def name(): String = "record"

}
