import Dependencies._
import build._

lazy val root = Project(
  id = "etcd4s",
  base = file(".")
).aggregate(core, akkaStream)
 .settings(etcd4sSettings)

lazy val core = Project(
  id = "etcd4s-core",
  base = file("etcd4s-core")
).settings(etcd4sSettings ++ Seq(
  name := "etcd4s-core",
  libraryDependencies ++= coreDepns,
  coverageExcludedPackages := "org.etcd4s.pb.*",
  PB.targets in Compile := Seq(
    scalapb.gen(
      flatPackage = true,
      javaConversions = false,
      grpc = true,
      singleLineToString = true) -> (sourceManaged in Compile).value
  )
))

lazy val akkaStream = Project(
  id = "etcd4s-akka-stream",
  base = file("etcd4s-akka-stream")
).dependsOn(core % "compile;test->test")
 .settings(etcd4sSettings ++ Seq(
   name := "etcd4s-akka-stream",
   libraryDependencies ++= akkaStreamDepns
 ))
