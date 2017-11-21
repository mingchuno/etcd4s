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
      client.kvService.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue
    }

    scenario(s"set '$KEY' to '$VALUE_1'") {
      client.kvService.put(PutRequest().withKey(KEY).withValue(VALUE_1)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_1'") {
      val result = client.kvService.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_1
    }

    scenario(s"update '$KEY' to '$VALUE_2'") {
      client.kvService.put(PutRequest().withKey(KEY).withValue(VALUE_2)).futureValue
    }

    scenario(s"get '$KEY' should be '$VALUE_2'") {
      val result = client.kvService.range(RangeRequest().withKey(KEY)).futureValue
      result.count shouldBe 1
      result.more shouldBe false
      (result.kvs.head.value: String) shouldBe VALUE_2
    }

    scenario(s"remove '$KEY' should have 1 key") {
      client.kvService.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue.deleted shouldBe 1
    }

    scenario(s"get '$KEY' should be empty") {
      client.kvService.range(RangeRequest().withKey(KEY)).futureValue.count shouldBe 0
    }
  }

  feature("KV api with range of keys") {
    scenario("set a rage of keys in the same key space and get them out") {
      info("remove all key under foo/ first")
      client.kvService.deleteRange(DeleteRangeRequest().withPrefix("foo/")).futureValue

      info("add 2 keys under foo/")
      client.kvService.put(PutRequest().withKey("foo/bar1").withValue("Hello")).futureValue
      client.kvService.put(PutRequest().withKey("foo/bar2").withValue("World")).futureValue

      info("get them and check")
      val result = client.kvService.range(RangeRequest().withPrefix("foo/")).futureValue
      result.count shouldBe 2
      result.more shouldBe false
      (result.kvs.map(_.key: String): Seq[String]) shouldBe Seq("foo/bar1", "foo/bar2")
    }
  }
}
