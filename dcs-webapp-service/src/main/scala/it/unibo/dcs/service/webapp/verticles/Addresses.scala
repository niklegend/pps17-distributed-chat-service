package it.unibo.dcs.service.webapp.verticles

private[verticles] object Addresses {

  object rooms {

    private val prefix = "rooms"

    val created = s"$prefix.created"

    val deleted = s"$prefix.deleted"

    val joined = s"$prefix.joined"

    val left = s"$prefix.left"

  }

  object messages {

    private val prefix = "messages"

    val sent = s"$prefix.sent"

  }

  object users {
    private val prefix = "users"

    val offline = s"$users.offline"

    val online = s"$users.online"

    object hearthbeat {

      private val prefix = s"${users.prefix}.hearthbeat"

      val request = s"$prefix.request"

      val response = s"$prefix.response"

    }

  }

}
