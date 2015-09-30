package foo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.io.InputStreamSource
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSpec, Matchers}

class ParseJsonTest extends FunSpec with BeforeAndAfterAll with Matchers with ScalaFutures {
  implicit val actorSystem = ActorSystem("test-only")
  val json = "/sample.json"

  override protected def afterAll(): Unit = actorSystem.shutdown()

  it("parses json correctly") {
    implicit val materializer = ActorMaterializer()

    val inputStreamSource = InputStreamSource(() => this.getClass.getResourceAsStream(json))
    val personSource = ParseJson.parse(inputStreamSource, Person.JsonFormat.personFormat)

    val result = personSource.runFold(Seq.empty[Person])(_ :+ _)

    result.futureValue shouldBe Seq(
      Person("John Smith", 32),
      Person("Albert Einstein", 65),
      Person("Jane Smith", 1)
    )

  }


}
