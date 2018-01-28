package org.etcd4s

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global

trait Etcd4sFeatureSpec extends FeatureSpecLike with Matchers with ScalaFutures with OptionValues with BeforeAndAfterAll {

  implicit protected val futureConfig = PatienceConfig(timeout = scaled(Span(10, Seconds)))

  override def afterAll(): Unit = {
    client.shutdown()
  }

  // TODO: move to config later
  protected val client = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    )
    Etcd4sClient.newClient(config)
  }

  def getAuthClient = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    ).withCredential("root", "Admin123")
    Etcd4sClient.newClient(config)
  }

}
