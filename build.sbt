name := "scala-learn"

version := "0.1"

scalaVersion := "2.13.3"

//scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.2.0",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
)
