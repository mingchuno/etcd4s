package org.etcd4s

import org.etcd4s.implicits._
import org.etcd4s.pb.etcdserverpb._

class KVServiceSpec extends Etcd4sFeatureSpec {

  info("Key-Value API is the core part of etcd APIs")

  feature("Create, update, read and remove a single key-value pair") {
    val KEY = "foo"
    val VALUE_1 = "Hello"
    val VALUE_2 = "World"

    scenario(s"remove '$KEY'") {
      client.kvApi.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue
    }

    scenario(s"set '$KEY' to '$VALUE_1'") {
      client.kvApi.put(PutRequest().withKey(KEY).withValue(VALUE_1)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_1'") {
      val result = client.kvApi.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_1
    }

    scenario(s"update '$KEY' to '$VALUE_2'") {
      client.kvApi.put(PutRequest().withKey(KEY).withValue(VALUE_2)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_2'") {
      val result = client.kvApi.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_2
    }

    scenario(s"remove '$KEY' should have 1 key") {
      client.kvApi
        .deleteRange(DeleteRangeRequest().withKey(KEY))
        .futureValue
        .deleted shouldBe 1
    }

    scenario(s"get '$KEY' should be empty") {
      client.kvApi.range(RangeRequest().withKey(KEY)).futureValue.count shouldBe 0
    }
  }

  feature("KV api with range of keys") {
    scenario("set a rage of keys in the same key space and get them out") {
      info("remove all key under foo/ first")
      client.kvApi.deleteRange(DeleteRangeRequest().withPrefix("foo/")).futureValue

      info("add 2 keys under foo/")
      client.kvApi.put(PutRequest().withKey("foo/bar1").withValue("Hello")).futureValue
      client.kvApi.put(PutRequest().withKey("foo/bar2").withValue("World")).futureValue

      info("get them and check")
      val result = client.kvApi.range(RangeRequest().withPrefix("foo/")).futureValue
      result.count shouldBe 2
      result.more shouldBe false
      (result.kvs.map(_.key: String): Seq[String]) shouldBe Seq("foo/bar1", "foo/bar2")

      client.kvApi
        .deleteRange(DeleteRangeRequest().withPrefix("foo/bar1"))
        .futureValue
        .deleted shouldBe 1
      client.kvApi
        .deleteRange(DeleteRangeRequest().withKey("foo/bar2"))
        .futureValue
        .deleted shouldBe 1
    }
  }

  feature("range operation") {

    val data = Map("foo/1" -> "bar1", "foo/2" -> "bar2", "foo/3" -> "bar3")

    scenario("set multiple keys") {
      data.foreach {
        case (k, v) =>
          client.kvApi.put(PutRequest(key = k, value = v)).futureValue
      }
    }

    scenario("get multiple keys") {
      val result: RangeResponse =
        client.kvApi.range(RangeRequest().withPrefix("foo/")).futureValue
      result.count shouldBe 3
      result.more shouldBe false
      val map: Map[String, String] = result.kvs.map { kv =>
        (kv.key: String, kv.value: String)
      }.toMap
      map shouldBe data
    }

    scenario("delete range") {
      client.kvApi
        .deleteRange(DeleteRangeRequest().withPrefix("foo/"))
        .futureValue
        .deleted shouldBe 3
      client.kvApi.range(RangeRequest().withPrefix("foo/")).futureValue.count shouldBe 0
    }
  }
}
