package it.unibo.dcs.service.user

import java.util.Date

trait User {

  def username: String

  def firstName: String

  def lastName: String

  def bio: String

  def visible: Boolean

  def lastSeen: Date

}
