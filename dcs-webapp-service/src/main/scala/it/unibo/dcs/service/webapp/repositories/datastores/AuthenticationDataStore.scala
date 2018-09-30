package it.unibo.dcs.service.webapp.repositories.datastores

trait AuthenticationDataStore {
  def loginUser(): Boolean

  def registerUser(): Boolean

  def logoutUser(): Boolean
}
