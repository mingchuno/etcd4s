package org.etcd4s

import org.etcd4s.pb.etcdserverpb._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.etcd4s.implicits._
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global

class ExampleConversionSpec
    extends FeatureSpecLike
    with Matchers
    with ScalaFutures
    with OptionValues
    with BeforeAndAfterAll {

  implicit protected val futureConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(10, Seconds))
  )

  protected lazy val rpcClient: Etcd4sRpcClient = {
    val config = Etcd4sClientConfig(
      address = "127.0.0.1",
      port = 2379
    )
    Etcd4sRpcClient.newClient(config)
  }

  override def afterAll(): Unit = {
    rpcClient.shutdown()
  }

  feature("test using RPC only") {
    val KEY = "foo"
    val VALUE_1 = "Hello"
    val VALUE_2 = "World"
    scenario(s"remove '$KEY'") {
      rpcClient.kvApi.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue
    }
    scenario(s"set '$KEY' to '$VALUE_1'") {
      rpcClient.kvApi.put(PutRequest().withKey(KEY).withValue(VALUE_1)).futureValue
    }
    scenario(s"get '$KEY' should be '$VALUE_1'") {
      val result = rpcClient.kvApi.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_1
    }
    scenario(s"update '$KEY' to '$VALUE_2'") {
      rpcClient.kvApi.put(PutRequest().withKey(KEY).withValue(VALUE_2)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_2'") {
      val result = rpcClient.kvApi.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_2
    }

    scenario(s"remove '$KEY' should have 1 key") {
      rpcClient.kvApi
        .deleteRange(DeleteRangeRequest().withKey(KEY))
        .futureValue
        .deleted shouldBe 1
    }

    scenario(s"get '$KEY' should be empty") {
      rpcClient.kvApi.range(RangeRequest().withKey(KEY)).futureValue.count shouldBe 0
    }
    scenario(s"set '$KEY' to '$VALUE_1' with lease and revoke the lease") {
      val leaseId = rpcClient.leaseApi.leaseGrant(LeaseGrantRequest(20)).futureValue.iD
      rpcClient.kvApi
        .put(PutRequest().withKey(KEY).withValue(VALUE_1).withLease(leaseId))
        .futureValue
      rpcClient.leaseApi.leaseRevoke(LeaseRevokeRequest(leaseId)).futureValue
      rpcClient.kvApi.range(RangeRequest().withKey(KEY)).futureValue.count shouldBe 0
    }
  }
}
