package com.codingkapoor.pageselectionbykeyword

import java.io.File
import java.io.FileWriter

import org.scalatest.Matchers
import org.scalatest.Outcome
import org.scalatest.fixture

import com.codingkapoor.pageselectionbykeyword.selector.SimplePageSelector
import com.codingkapoor.pageselectionbykeyword.util.SimpleUserInputFileReader

class PageSelectorSpec extends SimplePageSelector with SimpleUserInputFileReader with fixture.FlatSpecLike with Matchers {

  case class FixtureParam(file: File, writer: FileWriter)

  def withFixture(test: OneArgTest): Outcome = {
    val file = File.createTempFile("test", "txt")
    val writer = new FileWriter(file)

    val theFixture = FixtureParam(file, writer)

    try {
      withFixture(test.toNoArgTest(theFixture))
    } finally {
      writer.close()
      file.delete()
    }
  }

  "PageSelector" should "return expected result when the user input is ill formatted" in { f =>
    f.writer.write("P Ford Car Review                                 \n")
    f.writer.write("P                    Review Car\n")
    f.writer.write("Q Ford Review\n")
    f.writer.write("Q Ford Car\n")
    f.writer.write("Q cooking French\n")
    f.writer.write("P")

    f.writer.flush()

    val result = pageSelectionAlgo(Some(f.file.getAbsolutePath))
    result should equal(List(("1", List("0.1", "0.2")), ("2", List("0.1", "0.2")), ("3", List())))
  }

  "PageSelector" should "return expected result irrespective of cases of keywords" in { f =>
    f.writer.write("P FORD car REvIew\n")
    f.writer.write("P RevieW CaR\n")
    f.writer.write("Q Ford ReVIEW\n")
    f.writer.write("Q Ford Car\n")
    f.writer.write("Q cookING French\n")
    f.writer.write("P")

    f.writer.flush()

    val result = pageSelectionAlgo(Some(f.file.getAbsolutePath))
    result should equal(List(("1", List("0.1", "0.2")), ("2", List("0.1", "0.2")), ("3", List())))
  }

  "PageSelector" should "return expected result irrespective of order of listing of pages and queries in the user input" in { f =>
    f.writer.write("Q Ford Review\n")
    f.writer.write("P Ford Car Review\n")
    f.writer.write("Q Ford Car\n")
    f.writer.write("P Review Car\n")
    f.writer.write("Q cooking French\n")
    f.writer.write("P")

    f.writer.flush()

    val result = pageSelectionAlgo(Some(f.file.getAbsolutePath))
    result should equal(List(("1", List("0.1", "0.2")), ("2", List("0.1", "0.2")), ("3", List())))
  }

  "PageSelector" should "return expected result when there are dangling pages in the user input" in { f =>
    f.writer.write("P Ford Car Review\n")
    f.writer.write("P Review Car\n")
    f.writer.write("PPPP Review Car\n")
    f.writer.write("PPPPP Review Car\n")
    f.writer.write("Q Ford Review\n")
    f.writer.write("Q Ford Car\n")
    f.writer.write("Q cooking French\n")
    f.writer.write("P")

    f.writer.flush()

    val result = pageSelectionAlgo(Some(f.file.getAbsolutePath))
    result should equal(List(("1", List("0.1", "0.2")), ("2", List("0.1", "0.2")), ("3", List())))
  }

  "PageSelector" should "return expected result when there are nested pages in the user input" in { f =>
    f.writer.write("P Ford Car Review\n")
    f.writer.write("P Review Car\n")
    f.writer.write("PP Review Car\n")
    f.writer.write("PP Review Car\n")
    f.writer.write("Q Ford Review\n")
    f.writer.write("Q Ford Car\n")
    f.writer.write("Q cooking French\n")
    f.writer.write("P")

    f.writer.flush()

    val result = pageSelectionAlgo(Some(f.file.getAbsolutePath))
    result should equal(List(("1", List("0.2", "0.1")), ("2", List("0.2", "0.1")), ("3", List())))
  }

  "PageSelector" should "return expected result for a regular input" in { f =>
    f.writer.write("P Ford Car Review\n")
    f.writer.write("P Review Car\n")
    f.writer.write("Q Ford Review\n")
    f.writer.write("Q Ford Car\n")
    f.writer.write("Q cooking French\n")
    f.writer.write("P")

    f.writer.flush()

    val result = pageSelectionAlgo(Some(f.file.getAbsolutePath))
    result should equal(List(("1", List("0.1", "0.2")), ("2", List("0.1", "0.2")), ("3", List())))
  }

}
