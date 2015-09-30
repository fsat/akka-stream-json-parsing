package foo

import akka.stream.scaladsl.Source
import akka.util.ByteString
import play.api.libs.json.Format

object ParseJson {
  def parse[T](source: Source[ByteString, T], jsonFormat: Format[Person]): Source[Person, T] = ???
}
