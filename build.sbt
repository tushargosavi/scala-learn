name := "scala-learn"

version := "0.1"

scalaVersion := "2.13.3"

scalacOptions ++= Seq(
  "-Ymacro-annotations"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "3.2.5",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "org.typelevel" %% "simulacrum" % "1.0.1",
  "org.typelevel" %% "cats-parse" % "0.3.4",
  "org.tpolecat" %% "atto-core"    % "0.9.5",
  "org.tpolecat" %% "atto-refined" % "0.9.5",
  "com.github.pureconfig" %% "pureconfig" % "0.16.0",
  "co.fs2" %% "fs2-core" % "3.1.1",
  "co.fs2" %% "fs2-io" % "3.1.1",
  "co.fs2" %% "fs2-reactive-streams" % "3.1.1",
  //"co.fs2" %% "fs2-experimental" % "3.1.1",
  "io.netty" % "netty-all" % "4.1.63.Final",
  "io.searchbox" % "jest" % "6.3.1",
)
