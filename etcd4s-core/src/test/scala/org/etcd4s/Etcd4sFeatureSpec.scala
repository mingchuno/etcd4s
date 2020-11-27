package org.etcd4s

import org.scalatest._
import org.scalatest.featurespec.AnyFeatureSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global

trait Etcd4sFeatureSpec
    extends AnyFeatureSpecLike
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
  protected val client: Etcd4sClient = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    )
    Etcd4sClient.newClient(config)
  }

  def getAuthClient: Etcd4sClient = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    ).withCredential("root", "Admin123")
    Etcd4sClient.newClient(config)
  }

}
