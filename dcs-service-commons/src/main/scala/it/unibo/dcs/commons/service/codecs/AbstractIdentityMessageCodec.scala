package it.unibo.dcs.commons.service.codecs

abstract class AbstractIdentityMessageCodec[T] extends AbstractMessageCodec[T, T] {

  override final def transform(t: T): T = t

}
