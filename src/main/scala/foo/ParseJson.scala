package foo

import akka.stream.scaladsl.Source
import akka.stream.stage._
import akka.util.ByteString
import play.api.libs.json.{JsSuccess, Json, Format}

import scala.util.{Try, Success}

object ParseJson {
  def parse[T,X](source: Source[ByteString, T], jsonFormat: Format[X]): Source[X, T] =
    source.transform(() => new BufferedJsonParsingStage(jsonFormat))

  class BufferedJsonParsingStage[T](jsonFormat: Format[T]) extends DetachedStage[ByteString, T] {
    var buffer: Seq[ByteString] = Seq.empty

    override def onPush(elem: ByteString, ctx: DetachedContext[T]): UpstreamDirective = {
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
        case Success(parsedJson) =>
          jsonFormat.reads(parsedJson) match {
            case JsSuccess(obj, _) =>
              reset()
              ctx.pushAndPull(obj)
            case _ =>
              reset()
              ctx.pull()
          }
        case _ =>
          ctx.pull()
      }
    }

    override def onPull(ctx: DetachedContext[T]): DownstreamDirective =
      if (ctx.isHoldingUpstream)
        ctx.holdDownstreamAndPull()
      else
        ctx.holdDownstream()

    private def reset(): Unit =
      buffer = Seq.empty

  }
}
