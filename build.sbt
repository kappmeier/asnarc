enablePlugins(ScalaJSPlugin)

name := "Asnarc"

version := "1.0"

scalaVersion := "2.13.14"

// This is an application with a main method
scalaJSUseMainModuleInitializer := false

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "2.2.0",

  // Tests
  "org.scalatest" %%% "scalatest" % "3.2.19" % "test"
)

coverageExcludedPackages := ".*AsnarcJSRenderer.*"
