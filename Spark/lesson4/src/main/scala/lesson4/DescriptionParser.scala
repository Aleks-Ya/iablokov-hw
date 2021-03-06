package lesson4

import lesson4.Category.Category
import org.slf4j.LoggerFactory

//TODO remove parsing patternCategoryValue
class DescriptionParser(content: String) extends Serializable {
  private val log = LoggerFactory.getLogger(getClass)
  //  var content: String = _
  private val patternTitle =
    """^(\d+)\) (\w[,()'/\w\s]*): (\w+)$""".r
  private val patternCategoryValue = """^\t(\d+): ([\w\p{Punct}][\w\s\p{Punct}]*)$""".r

  lazy val allFields: Map[Int, Description] = parseFromString(content)
  lazy val numericFields: Map[Int, Description] = allFields.filter(tuple => tuple._2.category == Category.Numeric)
  lazy val categoricalFields: Map[Int, Description] = allFields.filter(tuple => tuple._2.category == Category.Categorical)

  def parseFromString(content: String): Map[Int, Description] = {
    val properties = content.split("\n\n").filter(_.nonEmpty)
    log.info("Properties count: " + properties.length)
    val propertiesMap = properties.map { property =>
      val lines = property.split("\n").filter(_.nonEmpty)
      val titleLine = lines.head
      val titleMatchesOpt = patternTitle.findFirstMatchIn(titleLine)
      if (titleMatchesOpt.nonEmpty) {
        val titleMatches = titleMatchesOpt.get
        val id = titleMatches.group(1).toInt - 1
        val title = titleMatches.group(2)
        val category = Category.fromString(titleMatches.group(3))

        val values = lines.tail.map(line => {
          val matchesOpt = patternCategoryValue.findFirstMatchIn(line)
          if (matchesOpt.nonEmpty) {
            val matches = matchesOpt.get
            val id = matches.group(1).toInt
            val title = matches.group(2).trim
            (id, title)
          } else {
            log.warn(s"Can't parse '$line'")
            null
          }
        }).filter(_ != null).toList

        (id, new Description(id, title, category, values))
      } else {
        log.warn(s"Can't parse titleLine '$titleLine'")
        null
      }
    }.filter(_ != null).toMap
    log.info("Properties map size: " + propertiesMap.size)
    if (propertiesMap.contains(50)) {
      val mutable = collection.mutable.Map(propertiesMap.toSeq: _*)
      mutable.remove(50)
      return Map(mutable.toSeq: _*)
    }
    propertiesMap
  }
}

class Description(val id: Int, val title: String, val category: Category, val values: List[(Int, String)])
  extends Serializable {}

object Category extends Enumeration {
  type Category = Value
  val Numeric, Categorical = Value

  def fromString(s: String): Category = {
    s.toUpperCase match {
      case "NUMERIC" => Numeric
      case "CATEGORICAL" => Categorical
      case _ => throw new IllegalArgumentException
    }
  }

}

