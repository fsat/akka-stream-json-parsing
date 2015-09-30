package foo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.io.InputStreamSource
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSpec, Matchers}
import scala.concurrent.duration._

class StreamingJsonParseTest extends FunSpec with BeforeAndAfterAll with Matchers with ScalaFutures {

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(timeout = 1 second)

  implicit val actorSystem = ActorSystem("test-only")
  val json = "/sample.json"

  override protected def afterAll(): Unit = actorSystem.shutdown()

  it("parses json correctly") {
    implicit val materializer = ActorMaterializer()

    // read 1 byte at a time
    val inputStreamSource = InputStreamSource(() => this.getClass.getResourceAsStream(json))
    val personSource = StreamingJsonParse.parse(inputStreamSource, Person.JsonFormat.personFormat)

    val result = personSource.runFold(Seq.empty[Person])(_ :+ _)

    result.futureValue shouldBe Seq(
      Person("John Smith", 32),
      Person("Albert Einstein", 65),
      Person("Jane Smith", 29)
    )

  }


}
