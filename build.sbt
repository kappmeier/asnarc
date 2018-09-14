enablePlugins(ScalaJSPlugin) // , WorkbenchPlugin

name := "Asnarc"

version := "1.0"

scalaVersion := "2.12.6"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  "com.lihaoyi" %%% "scalatags" % "0.6.5",

  // Tests
  "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
)
