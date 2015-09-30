package foo

import akka.stream.scaladsl.Source
import akka.stream.stage._
import akka.util.ByteString
import play.api.libs.json.{JsSuccess, Json, Format}

import scala.util.{Try, Success}

object ParseJson {
  def parse[T](source: Source[ByteString, T], jsonFormat: Format[Person]): Source[Person, T] =
    source.transform(() => new BufferedJsonParsingStage(jsonFormat))

  class BufferedJsonParsingStage(jsonFormat: Format[Person]) extends DetachedStage[ByteString, Person] {
    var buffer: Seq[ByteString] = Seq.empty

    override def onPush(elem: ByteString, ctx: DetachedContext[Person]): UpstreamDirective = {
      val input = elem.utf8String
      if (input == "" ||
          input == "\n" ||
          input == "[" ||
          input == "]" ||
          (input == "," && buffer.isEmpty) ||
          (input == " " && buffer.isEmpty))
        return ctx.pull()

      buffer :+= elem
      val collectedString = buffer.map(_.utf8String).mkString("")
      Try { Json.parse(collectedString) } match {
        case Success(json) =>
          jsonFormat.reads(json) match {
            case JsSuccess(person, _) =>
              reset()
              ctx.pushAndPull(person)
            case _ =>
              reset()
              ctx.pull()
          }
        case _ =>
          ctx.pull()
      }
    }

    override def onPull(ctx: DetachedContext[Person]): DownstreamDirective =
      if (ctx.isHoldingUpstream)
        ctx.holdDownstreamAndPull()
      else
        ctx.holdDownstream()

    private def reset(): Unit =
      buffer = Seq.empty

  }
}
