package org.etcd4s.implicits

import com.google.protobuf.ByteString
import org.etcd4s.pb.etcdserverpb.RangeRequest
import org.scalatest._

class ImplicitsOpsSpec extends FeatureSpec with Matchers {
  feature("RequestPrefixOps should be able to convert given key to range end") {
    scenario("simple foo/ range") {
      val req = RangeRequest().withPrefix("foo/")
      (req.key: String) shouldBe "foo/"
      (req.rangeEnd: String) shouldBe "foo0"
    }

    scenario("other ascii chars should work too") {
      val req = RangeRequest().withPrefix("foo~")
      (req.key: String) shouldBe "foo~"
      req.rangeEnd.toByteArray shouldBe Array[Byte](102,111,111,127)
    }

    scenario("should return []byte{0} for 0xff") {
      val FF = Array[Byte](-1)
      val ZERO = Array[Byte](0)
      val req = RangeRequest().withPrefix(ByteString.copyFrom(FF))
      req.key.toByteArray shouldBe FF
      req.rangeEnd.toByteArray shouldBe ZERO
    }

    scenario("should return b for a0xff") {
      val AFF = Array[Byte](97, -1)
      val B = Array[Byte](98)
      val req = RangeRequest().withPrefix(ByteString.copyFrom(AFF))
      req.key.toByteArray shouldBe AFF
      req.rangeEnd.toByteArray shouldBe B
    }

    scenario("should wrap around with Byte.MaxValue") {
      val MAX = Array[Byte](Byte.MaxValue)
      val MIN = Array[Byte](Byte.MinValue)
      val req = RangeRequest().withPrefix(ByteString.copyFrom(MAX))
      req.key.toByteArray shouldBe MAX
      req.rangeEnd.toByteArray shouldBe MIN
    }

    scenario("try to CKJ chars") {
      val ckjStr = "你好"
      val arry: Array[Byte] = ckjStr.getBytes("UTF-8")

      val req = RangeRequest().withPrefix(ByteString.copyFrom(arry))
      req.key.toByteArray shouldBe arry
      req.rangeEnd.toByteArray shouldBe Array(-28, -67, -96, -27, -91, -66).map(_.toByte)
    }
  }
}
