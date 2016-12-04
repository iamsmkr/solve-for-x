package com.codingkapoor

import org.scalatest.{ FlatSpec, Matchers }

class BomberIIISpec extends FlatSpec with Matchers {
  "bomber" should "return string dropped of all consecutive similar letters" in {
    BomberIII.bomber("") should equal("")
    BomberIII.bomber("abbabba") should equal("")
    BomberIII.bomber("aabcccdee") should equal("bd")
    BomberIII.bomber("abcdeedcbfgf") should equal("afgf")
  }
}