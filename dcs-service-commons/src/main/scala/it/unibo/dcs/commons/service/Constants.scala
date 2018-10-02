package it.unibo.dcs.commons.service

private[service] object Constants {

  private val CHANNEL_PREFIX: String = "discovery.http-endpoint"

  val PUBLISH_CHANNEL: String = CHANNEL_PREFIX + ".published"
  val UNPUBLISH_CHANNEL: String = CHANNEL_PREFIX + ".unpublished"

}
