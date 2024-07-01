ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.4"

lazy val root = (project in file("."))
  .settings(
    name := "ReferentialTransparency"
  )
