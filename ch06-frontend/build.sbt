
name := "frontend"
version := "0.0.1"
scalaVersion := "2.12.4"

val akkaVersion = "2.5.6"

libraryDependencies in Global ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
  "com.typesafe.akka" %% "akka-remote"  % akkaVersion
)
