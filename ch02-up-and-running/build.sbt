
name := "goticks"
version := "0.0.1"
scalaVersion := "2.12.4"

val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"

libraryDependencies in Global ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"  % akkaVersion,
  "com.typesafe.akka" %% "akka-http"    % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

  "com.typesafe.akka" %% "akka-testkit"  % akkaVersion  % "test",
  "org.scalatest"     %% "scalatest"     % "3.0.4"      % "test"
)
