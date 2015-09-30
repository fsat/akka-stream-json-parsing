package foo

import play.api.libs.json._

object Person {
  object JsonFormat {
    val personFormat: Format[Person] = Json.format
  }
}

case class Person(name: String, age: Int)
