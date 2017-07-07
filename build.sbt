enablePlugins(ScalaJSPlugin, WorkbenchPlugin)

name := "Asnarc"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.3",
  "com.lihaoyi" %%% "scalatags" % "0.6.5"
)
