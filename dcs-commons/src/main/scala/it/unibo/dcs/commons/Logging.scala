package it.unibo.dcs.commons

import io.vertx.lang.scala.ScalaLogger

trait Logging {

  private[this] var _log: ScalaLogger = _

  protected def log: ScalaLogger = {
    if (_log == null) {
      synchronized {
        if (_log == null) {
          _log = ScalaLogger.getLogger(getClass.getName)
        }
      }
    }
    _log
  }

}
