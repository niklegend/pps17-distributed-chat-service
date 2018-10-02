package it.unibo.dcs.service.webapp.model

import java.util.Date

case class User(username: String, firstName: String, lastName: String, bio: String, visible: Boolean, lastSeen: Date)
