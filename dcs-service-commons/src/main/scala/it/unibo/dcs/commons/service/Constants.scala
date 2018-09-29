package it.unibo.dcs.commons.service

private[service] object Constants {

  val CHANNEL_PREFIX: String = "discovery.records"
  val PUBLISH_CHANNEL: String = CHANNEL_PREFIX + ".published"
  val UNPUBLISH_CHANNEL: String = CHANNEL_PREFIX + ".unpublished"

}
