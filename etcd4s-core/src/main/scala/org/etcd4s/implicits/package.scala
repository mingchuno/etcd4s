package org.etcd4s

import com.google.protobuf.ByteString
import org.etcd4s.pb.etcdserverpb.{DeleteRangeRequest, RangeRequest}

import scala.collection.mutable.ArrayBuffer

package object implicits extends ByteStringOps with StringOps with RequestPrefixOps

trait ByteStringOps {
  implicit def decode(bytes: ByteString): String = {
    new String(bytes.toByteArray)
  }
}

trait StringOps {
  implicit def encode(str: String): ByteString = {
    ByteString.copyFrom(str.getBytes("UTF-8"))
  }
}

trait RequestPrefixOps {

  private val noPrefixEnd = ByteString.copyFrom(Array[Byte](0))

  private def getPrefix(key: ByteString): ByteString = {
    val bytes = key.toByteArray
    val result = ArrayBuffer[Byte]()
    for (i <- (bytes.length - 1) to 0 by -1) {
      if (bytes(i) < Byte.MaxValue) {
        for (j <- 0 until i) {
          result += bytes(j)
        }
        result += (bytes(i) + 1).toByte
        return ByteString.copyFrom(result.toArray)
      }
    }
    noPrefixEnd
  }

  implicit class RangeRequestOps(request: RangeRequest) {
    def withPrefix(__v: ByteString): RangeRequest = {
      request.withKey(__v).withRangeEnd(getPrefix(__v))
    }
  }

  implicit class DeleteRangeRequestOps(request: DeleteRangeRequest) {
    def withPrefix(__v: ByteString): DeleteRangeRequest = {
      request.withKey(__v).withRangeEnd(getPrefix(__v))
    }
  }

}
