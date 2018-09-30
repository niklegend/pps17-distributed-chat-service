package it.unibo.dcs.commons.interactor

import rx.lang.scala.Scheduler
import rx.lang.scala.schedulers.ExecutionContextScheduler

import scala.concurrent.ExecutionContext

package object executor {

  trait ThreadExecutor extends ExecutionContext

  final case class PostExecutionThread(private[executor] val scheduler: Scheduler)

  private[interactor] object Implicits {

    implicit def executionContextToScheduler(te: ThreadExecutor): Scheduler = ExecutionContextScheduler(te)

    implicit def postExecutionThreadToScheduler(pet: PostExecutionThread): Scheduler = pet.scheduler

  }

}
