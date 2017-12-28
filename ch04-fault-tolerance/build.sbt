
name := "fault-tolerance"
version := "0.0.1"
scalaVersion := "2.12.4"

val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"

libraryDependencies in Global ++= Seq(
  "com.typesafe.akka"   %%  "akka-actor"   % akkaVersion,
  "com.typesafe.akka"	%%  "akka-slf4j"   % akkaVersion,
  "com.typesafe.akka"	%%  "akka-testkit" % akkaVersion   % "test",
  "org.scalatest"       %%  "scalatest"    % "3.0.4"       % "test"
)
