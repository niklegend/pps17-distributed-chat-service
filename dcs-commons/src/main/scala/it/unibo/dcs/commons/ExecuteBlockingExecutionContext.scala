package it.unibo.dcs.commons

import io.vertx.lang.scala.ScalaLogger
import io.vertx.scala.core.Vertx

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ExecuteBlockingExecutionContext(vertx: Vertx) extends ExecutionContext {

  private val logger = ScalaLogger.getLogger(getClass.getName)

  override def execute(runnable: Runnable): Unit = vertx.executeBlocking[Unit](runnable.run).onComplete {
    case Success(_) =>
    case Failure(cause) => reportFailure(cause)
  }

  override def reportFailure(cause: Throwable): Unit = {
    logger.error("Failed executing on default worker executor", cause)
  }

}

object ExecuteBlockingExecutionContext {

  def apply(vertx: Vertx): ExecuteBlockingExecutionContext = new ExecuteBlockingExecutionContext(vertx)
  
}
