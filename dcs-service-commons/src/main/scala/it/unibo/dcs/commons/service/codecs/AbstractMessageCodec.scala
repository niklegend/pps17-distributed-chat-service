package it.unibo.dcs.commons.service.codecs

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.lang.scala.json.JsonObject

abstract class AbstractMessageCodec[S, R] extends MessageCodec[S, R] {

  override final def encodeToWire(buffer: Buffer, s: S): Unit = {
    val jsonObject = encodeToWire(s)

    val encoded = jsonObject.encode()
    val length = encoded.length()

    buffer.appendInt(length)
    buffer.appendString(encoded)
  }

  override final def decodeFromWire(pos: Int, buffer: Buffer): R = {
    var p = pos
    val length = buffer.getInt(p)
    p += 4
    val decoded = new JsonObject(buffer.getString(p, p + length))

    decodeFromWire(decoded)
  }

  override def systemCodecID(): Byte = -1

  protected def encodeToWire(s: S): JsonObject

  protected def decodeFromWire(jsonObject: JsonObject): R

}
