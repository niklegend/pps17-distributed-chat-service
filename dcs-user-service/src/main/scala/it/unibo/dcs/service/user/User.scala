package it.unibo.dcs.service.user

import java.util.Date

case class User(username: String, firstName: String, lastName: String, bio: String, visible: Boolean, lastSeen: Date)
