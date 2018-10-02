package org.etcd4s

import com.google.protobuf.ByteString
import org.etcd4s.pb.etcdserverpb.{DeleteRangeRequest, RangeRequest, WatchCreateRequest}

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
    for (i <- (bytes.length - 1) to 0 by -1) {
      if (bytes(i) != -1) {
        bytes.update(i, (bytes(i) + 1).toByte)
        return ByteString.copyFrom(bytes, 0, i + 1)
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

  implicit class WatchCreateRequestOps(request: WatchCreateRequest) {
    def withPrefix(__v: ByteString): WatchCreateRequest = {
      request.withKey(__v).withRangeEnd(getPrefix(__v))
    }
  }

}
