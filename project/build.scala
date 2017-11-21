import sbt._
import Keys._

object build {
  val etcd4sSettings = Seq(
    organization := "org.etcd4s",
    scalaVersion := "2.12.4",
    version      := "0.1.0-SNAPSHOT",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:postfixOps",
      "-Xfuture"
    ),
    parallelExecution in Test := false,
    fork in Test := true,
    testOptions in Test += Tests.Argument("-oD")
  )

  // val noPublish = Seq(
  //   mimaPreviousArtifacts := Set(),
  //   publishArtifact := false,
  //   publish := {},
  //   publishLocal := {}
  // )
}