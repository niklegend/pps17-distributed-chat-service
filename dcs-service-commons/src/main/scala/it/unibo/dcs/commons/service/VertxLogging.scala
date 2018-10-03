package it.unibo.dcs.commons.service

import io.vertx.core.logging.{Logger, LoggerFactory}

trait VertxLogging {

  private[this] val logger = LoggerFactory.getLogger(getClass)

  protected final def log: Logger = logger

}
