package it.unibo.dcs.commons.validation

import org.scalatest.FlatSpec

final class ValidatorTest extends FlatSpec {

  val validator = Validator[String] {
    _
      .addRule(_.length > 0, l => new IllegalArgumentException("String is empty"))
      .addRule(_.length < 15, l => new IllegalArgumentException("Length is greater than 15"))
  }

  it should "throw a IllegalArgumentException with message \"String is emtpy\"" in {
    val thrown = intercept[IllegalArgumentException] {
      validator.validate("")
    }
    assert(thrown.getMessage == "String is empty")
  }

  it should "throw a IllegalArgumentException with message \"Length is greater than 15\"" in {
    val thrown = intercept[IllegalArgumentException] {
      validator.validate("reallylongusername")
    }
    assert(thrown.getMessage == "Length is greater than 15")
  }

}
