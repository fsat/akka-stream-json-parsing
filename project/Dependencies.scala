import sbt._
import sbt.Resolver.bintrayRepo

object Version {
  val akka                   = "2.3.11"
  val akkaHttp               = "1.0"
  val akkaStream             = "1.0"
  val play                   = "2.4.0"

  val scala                  = "2.11.7"
  val scalaTest              = "2.2.4"
  val mockito                = "1.9.5"
}

object Library {
  val akkaActor               = "com.typesafe.akka"      %% "akka-actor"                     % Version.akka
  val akkaHttp                = "com.typesafe.akka"      %% "akka-http-experimental"         % Version.akkaHttp
  val akkaStream              = "com.typesafe.akka"      %% "akka-stream-experimental"       % Version.akkaStream
  val playJson                = "com.typesafe.play"      %% "play-json"                      % Version.play
  val mockitoAll              = "org.mockito"            %  "mockito-all"                    % Version.mockito
  val scalaTest               = "org.scalatest"          %% "scalatest"                      % Version.scalaTest
  val akkaTestkit             = "com.typesafe.akka"      %% "akka-testkit"                   % Version.akka
}

object Resolver {
}
