package it.unibo.dcs.service.webapp.verticles.utils

import io.vertx.core.json.JsonObject
import io.vertx.scala.core.DeploymentOptions
import it.unibo.dcs.commons.VertxHelper

object DeploymentUtils {

  def deploymentOptions: DeploymentOptions = {
    DeploymentOptions() setConfig readServerConfiguration
  }

  def deploymentHost: String = {
    deploymentOptions.getConfig.getString("host")
  }

  def deploymentPort: Integer = {
    deploymentOptions.getConfig.getInteger("port")
  }

  def readServerConfiguration: JsonObject = {
    VertxHelper.readJsonObject("/config.json")
  }
}
