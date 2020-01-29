package org.etcd4s.services

import org.etcd4s.formats.Write
import org.etcd4s.implicits._
import org.etcd4s.pb.v3lockpb.{LockRequest, UnlockRequest, UnlockResponse}
import org.etcd4s.rpc.LockRpc

import scala.concurrent.{ExecutionContext, Future}

@deprecated
private[etcd4s] class LockService(protected val lockRpc: LockRpc) {
  def lock[K](
      name: K,
      lease: Long
  )(implicit write: Write[K], ec: ExecutionContext): Future[String] = {
    lockRpc.lock(LockRequest(name = write.write(name), lease)).map(_.key)
  }

  def unlock(key: String): Future[UnlockResponse] = {
    lockRpc.unlock(UnlockRequest(key))
  }
}
