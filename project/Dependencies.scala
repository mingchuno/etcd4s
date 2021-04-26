import sbt._
import scalapb.compiler.Version._

object Dependencies {
  private val scala_test    = "org.scalatest" %% "scalatest" % "3.2.3"
  // gRPC
  private val grpc_netty    = "io.grpc" % "grpc-netty" % grpcJavaVersion
  private val grpc_runtime  = "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion

  // akka stream
  private val akka_version = "2.6.14"
  private val akka_stream           = "com.typesafe.akka" %% "akka-stream" % akka_version
  private val akka_stream_testkit   = "com.typesafe.akka" %% "akka-stream-testkit" % akka_version
  private val akka_test_kit         = "com.typesafe.akka" %% "akka-testkit" % akka_version

  val coreDepns = Seq(
    grpc_netty,
    grpc_runtime,
    scala_test % Test
  )

  val akkaStreamDepns = Seq(
    akka_stream,
    akka_stream_testkit % Test,
    akka_test_kit % Test
  )
}
