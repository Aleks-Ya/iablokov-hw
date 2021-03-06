package lesson4

import org.scalatest.{FlatSpec, Matchers}

class DescriptionParserTest extends FlatSpec with Matchers {
  it should "parse categorical" in {
    val content =
      """39) Is employed, USD: CATEGORICAL
        |	0: employed/yet-al,re<ady
        |	1: not employed""".stripMargin
    val parser = new DescriptionParser(content)
    val description = parser.allFields(38)
    description.id shouldEqual 38
    description.title shouldEqual "Is employed, USD"
    description.category shouldEqual Category.Categorical
    description.values should have size 2
    description.values.head._1 shouldEqual 0
    description.values.head._2 shouldEqual "employed/yet-al,re<ady"
    description.values(1)._1 shouldEqual 1
    description.values(1)._2 shouldEqual "not employed"
  }


  it should "parse numeric" in {
    val content =
      """48) Average amount of the delayed payment, USD: NUMERIC"""
    val parser = new DescriptionParser(content)
    val description = parser.allFields(47)
    description.id shouldEqual 47
    description.title shouldEqual "Average amount of the delayed payment, USD"
    description.category shouldEqual Category.Numeric
    description.values should have size 0
  }

  it should "numerate descriptions from 0" in {
    val content =
      """48) Average amount of the delayed payment, USD: NUMERIC"""
    val parser = new DescriptionParser(content)
    println(parser.allFields)
    parser.allFields should have size 1
    parser.allFields should contain key 47
  }
}
