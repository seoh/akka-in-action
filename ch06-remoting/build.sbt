import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm
import sbt.TestResult

name := "goticks"
version := "0.0.2"
scalaVersion := "2.12.4"

val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"

libraryDependencies in Global ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"   % akkaVersion,
  "com.typesafe.akka" %% "akka-remote"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"  % akkaVersion,
  "com.typesafe.akka" %% "akka-http"    % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.qos.logback"    %  "logback-classic"      % "1.2.3",

  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)



lazy val multiJvmSettings = SbtMultiJvm.multiJvmSettings ++ Seq(
  compile in MultiJvm := (compile in MultiJvm).triggeredBy(compile in Test).value,

  // from http://www.scala-sbt.org/1.x/docs/Parallel-Execution.html#Defining+Restrictions
  concurrentRestrictions in Test := Seq(
    Tags.limit(Tags.Test, 1)
  ),

  executeTests in Test := {
    val testResults = (executeTests in Test).value
    val multiJvmResults = (executeTests in MultiJvm).value
    // sbt.TestResult.Value is not Enumeration any more. set priority manullay.
    val overall = (testResults.overall, multiJvmResults.overall) match {
      case (TestResult.Passed, TestResult.Failed | TestResult.Error) => multiJvmResults.overall
      case (TestResult.Failed, TestResult.Error) => multiJvmResults.overall
      case _ => testResults.overall
    }
    Tests.Output(overall,
      testResults.events ++ multiJvmResults.events,
      testResults.summaries ++ multiJvmResults.summaries)
  }
)

lazy val goticks = (project in file(".")).configs(MultiJvm).settings(
  multiJvmSettings ++ Seq(crossPaths := false)
)
