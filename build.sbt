import AssemblyKeys._
assemblySettings

name := "yeahcat"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.apache.tomcat" % "catalina" % "6.0.44"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0"

addCompilerPlugin(
  "org.scala-lang.plugins" % "scala-continuations-plugin_2.11.6" % "1.0.2")