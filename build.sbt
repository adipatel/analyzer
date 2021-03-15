name := "analyzer"

version := "0.1"

scalaVersion := "2.13.4"

enablePlugins(JavaAppPackaging)

mainClass in (Compile, run) := Some("analyzer.Application")

javaOptions in Universal ++= Seq(
  "-J-Xms64m",
  "-J-Xmx512m"
)

libraryDependencies ++= {
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "com.lihaoyi" %% "upickle" % "0.9.5",
    "com.tdunning" % "t-digest" %"3.2",
    "org.apache.commons" % "commons-math3" % "3.6.1",
    "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  )
}
