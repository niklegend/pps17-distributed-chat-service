package it.unibo.dcs.commons.validation

import scala.collection.mutable

final case class Rule[T](private[validation] val predicate: T => Boolean,
                         private[validation] val exception: T => Throwable)

final class ValidatorBuilder[T] private[validation]() {

  private[this] val rules = mutable.Buffer[Rule[T]]()

  def addRule(rule: Rule[T]): ValidatorBuilder[T] = {
    rules += rule
    this
  }

  def addRule(predicate: T => Boolean,
              exception: T => Throwable = _ => new RuntimeException()): ValidatorBuilder[T] = {
    addRule(Rule[T](predicate, exception))
  }

  private[validation] def build: Validator[T] = new ValidatorImpl[T](rules.toList)

}
