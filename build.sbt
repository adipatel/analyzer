name := "analyzer"

version := "0.1"

scalaVersion := "2.13.4"

mainClass in (Compile, run) := Some("analyzer.Application")

val AkkaVersion = "2.6.13"

libraryDependencies ++= {
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "com.lihaoyi" %% "upickle" % "0.9.5",
    "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "org.scalatest" %% "scalatest" % "3.2.2" % Test,
    "org.scalatest" %% "scalatest-funsuite" % "3.2.2" % Test
  )
}
