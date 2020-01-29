package org.etcd4s

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global

trait Etcd4sFeatureSpec
    extends FeatureSpecLike
    with Matchers
    with ScalaFutures
    with OptionValues
    with BeforeAndAfterAll {

  implicit protected val futureConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(10, Seconds))
  )

  override def afterAll(): Unit = {
    client.shutdown()
  }

  // TODO: move to config later
  protected val client: Etcd4sRpcClient = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    )
    Etcd4sRpcClient.newClient(config)
  }

  def getAuthClient: Etcd4sRpcClient = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    ).withCredential("root", "Admin123")
    Etcd4sRpcClient.newClient(config)
  }

}
