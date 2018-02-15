import sbt._
import Keys._
import xerial.sbt.Sonatype._

object build {
  val etcd4sSettings = Seq(
    organization := "com.github.mingchuno",
    scalaVersion := "2.12.4",
    version      := "0.1.2",
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
    testOptions in Test += Tests.Argument("-oD"),
    publishTo := SonatypeKeys.sonatypePublishTo.value,
    publishMavenStyle := true,
    homepage := Some(url("https://github.com/mingchuno/etcd4s")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/mingchuno/etcd4s"),
        "scm:git@github.com:mingchuno/etcd4s.git"
      )
    ),
    developers := List(
      Developer(id = "mingchuno", name = "Or Ming Chun", email = "mingchuno", url = url("https://github.com/mingchuno"))
    ),
    licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
  )
}