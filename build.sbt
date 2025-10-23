
val _scalaVersion = "3.7.2"

ThisBuild / version := "0.1.0"

organization := "io.github.makingthematrix"
name := "ReferentialTransparency"
licenses := Seq("GPL 3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html"))
ThisBuild / scalaVersion := _scalaVersion
ThisBuild / versionScheme := Some("semver-spec")
Test / scalaVersion := _scalaVersion

developers := List(
  Developer(
    "makingthematrix",
    "Maciej Gorywoda",
    "makingthematrix@protonmail.com",
    url("https://github.com/makingthematrix"))
)

val standardOptions = Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-encoding",
  "utf8"
)

val scala3Options = Seq(
  "-explain",
  "-Wsafe-init",
  "-Ycheck-all-patmat",
  "-Wunused:imports",
  "-no-indent", "-rewrite"
)

lazy val root = (project in file("."))
  .settings(
    name := "ReferentialTransparency",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.6.3",
      "org.slf4j" % "slf4j-simple" % "2.0.17",
      //Test dependencies
      "org.scalameta" %% "munit" % "1.2.1" % "test"
    ),
    scalacOptions ++= standardOptions ++ scala3Options
  )

testFrameworks += new TestFramework("munit.Framework")
