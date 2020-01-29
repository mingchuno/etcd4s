package org.etcd4s

import com.google.protobuf.ByteString
import org.etcd4s.pb.etcdserverpb.{DeleteRangeRequest, RangeRequest, WatchCreateRequest}

package object implicits extends ByteStringOps with StringOps with RequestPrefixOps

trait ByteStringOps {
  implicit def decode(bytes: ByteString): String = {
    bytes.toStringUtf8
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
    def withPrefix(prefix: ByteString): RangeRequest = {
      request.withKey(prefix).withRangeEnd(getPrefix(prefix))
    }
  }

  implicit class DeleteRangeRequestOps(request: DeleteRangeRequest) {
    def withPrefix(prefix: ByteString): DeleteRangeRequest = {
      request.withKey(prefix).withRangeEnd(getPrefix(prefix))
    }
  }

  implicit class WatchCreateRequestOps(request: WatchCreateRequest) {
    def withPrefix(prefix: ByteString): WatchCreateRequest = {
      request.withKey(prefix).withRangeEnd(getPrefix(prefix))
    }
  }

}
