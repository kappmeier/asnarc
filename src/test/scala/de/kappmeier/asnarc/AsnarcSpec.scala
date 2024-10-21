package de.kappmeier.asnarc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
  * Abstract base class for asnarc unit tests.
  */
abstract class AsnarcSpec extends AnyFlatSpec with should.Matchers
