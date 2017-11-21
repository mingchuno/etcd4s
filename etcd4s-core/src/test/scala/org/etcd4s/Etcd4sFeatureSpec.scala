package org.etcd4s

import java.util.concurrent.TimeUnit

import io.grpc.ManagedChannelBuilder
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

trait Etcd4sFeatureSpec extends FeatureSpecLike with Matchers with ScalaFutures with OptionValues with BeforeAndAfterAll {
  implicit protected val futureConfig = PatienceConfig(timeout = scaled(Span(10, Seconds)))
  // TODO: move to config later
  protected val client = {
    val channel = ManagedChannelBuilder.forAddress("127.0.0.1", 2379).usePlaintext(true).build()
    new Etcd4sClient(channel)
  }

  override def afterAll(): Unit = {
    client.channel.shutdown()
    client.channel.awaitTermination(5, TimeUnit.SECONDS)
  }
}
