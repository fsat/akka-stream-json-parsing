name := "akka-stream-json-parsing"

version := "0.0.1"

scalaVersion := Version.scala

libraryDependencies ++= List(
  Library.akkaActor,
  Library.akkaHttp,
  Library.akkaStream,
  Library.playJson,
  Library.scalaTest
)


