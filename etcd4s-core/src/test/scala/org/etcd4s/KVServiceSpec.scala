package org.etcd4s

import org.etcd4s.formats._
import org.etcd4s.implicits._
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.ExecutionContext.Implicits.global

class KVServiceSpec extends Etcd4sFeatureSpec {

  info("Key-Value API is the core part of etcd APIs")

  feature("Create, update, read and remove a single key-value pair") {
    val KEY = "foo"
    val VALUE_1 = "Hello"
    val VALUE_2 = "World"

    scenario(s"remove '$KEY'") {
      client.rpcClient.kvRpc.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue
    }

    scenario(s"set '$KEY' to '$VALUE_1'") {
      client.rpcClient.kvRpc.put(PutRequest().withKey(KEY).withValue(VALUE_1)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_1'") {
      val result = client.rpcClient.kvRpc.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_1
    }

    scenario(s"update '$KEY' to '$VALUE_2'") {
      client.rpcClient.kvRpc.put(PutRequest().withKey(KEY).withValue(VALUE_2)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_2'") {
      val result = client.rpcClient.kvRpc.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_2
    }

    scenario(s"remove '$KEY' should have 1 key") {
      client.rpcClient.kvRpc.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue.deleted shouldBe 1
    }

    scenario(s"get '$KEY' should be empty") {
      client.rpcClient.kvRpc.range(RangeRequest().withKey(KEY)).futureValue.count shouldBe 0
    }
  }

  feature("KV api with range of keys") {
    scenario("set a rage of keys in the same key space and get them out") {
      info("remove all key under foo/ first")
      client.rpcClient.kvRpc.deleteRange(DeleteRangeRequest().withPrefix("foo/")).futureValue

      info("add 2 keys under foo/")
      client.rpcClient.kvRpc.put(PutRequest().withKey("foo/bar1").withValue("Hello")).futureValue
      client.rpcClient.kvRpc.put(PutRequest().withKey("foo/bar2").withValue("World")).futureValue

      info("get them and check")
      val result = client.rpcClient.kvRpc.range(RangeRequest().withPrefix("foo/")).futureValue
      result.count shouldBe 2
      result.more shouldBe false
      (result.kvs.map(_.key: String): Seq[String]) shouldBe Seq("foo/bar1", "foo/bar2")

      client.kvService.deleteKey("foo/bar1").futureValue shouldBe 1
      client.kvService.deleteKey("foo/bar2").futureValue shouldBe 1
    }
  }

  feature("Create, update, read and remove using simplified APIs") {

    val KEY = "foo"
    val VALUE_1 = "Hello"
    val VALUE_2 = "World"

    scenario(s"remove '$KEY'") {
      client.kvService.deleteKey(KEY).futureValue
    }

    scenario(s"set '$KEY' to '$VALUE_1'") {
      client.kvService.setKey(KEY, VALUE_1).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_1'") {
      client.kvService.getKey(KEY).futureValue.get shouldBe VALUE_1
    }

    scenario(s"update '$KEY' to '$VALUE_2'") {
      client.kvService.setKey(KEY, VALUE_2).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_2'") {
      client.kvService.getKey(KEY).futureValue.get shouldBe VALUE_2
    }

    scenario(s"remove '$KEY' should have 1 key") {
      client.kvService.deleteKey(KEY).futureValue shouldBe 1
    }

    scenario(s"get '$KEY' should be empty") {
      client.kvService.getKey(KEY).futureValue shouldBe None
    }

  }

  feature("range operation") {

    val data = Map("foo/1" -> "bar1", "foo/2" -> "bar2", "foo/3" -> "bar3")

    scenario("set multiple keys") {
      data.foreach { case (k,v) =>
        client.kvService.setKey(k, v).futureValue
      }
    }

    scenario("get multiple keys") {
      val result: RangeResponse = client.kvService.getRange("foo/").futureValue
      result.count shouldBe 3
      result.more shouldBe false
      val map: Map[String, String] = result.kvs.map { kv =>
        (kv.key: String, kv.value: String)
      }.toMap
      map shouldBe data
    }

    scenario("delete range") {
      client.kvService.deleteRange("foo/").futureValue shouldBe 3
      client.kvService.getRange("foo/").futureValue.count shouldBe 0
    }
  }
}
