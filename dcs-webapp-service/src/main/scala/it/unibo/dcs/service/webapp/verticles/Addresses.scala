package it.unibo.dcs.service.webapp.verticles

private[verticles] object Addresses {

  object rooms {

    private val prefix = "rooms"

    val created = s"$prefix.created"

    val deleted = s"$prefix.deleted"

  }

}
