package org.etcd4s.services

import org.etcd4s.formats.Write
import org.etcd4s.implicits._
import org.etcd4s.pb.v3lockpb.{LockRequest, UnlockRequest, UnlockResponse}
import org.etcd4s.rpc.LockRpc

import scala.concurrent.{ExecutionContext, Future}

trait LockService {
  val lockApi: LockRpc
  def lock[K](
      name: K,
      lease: Long
  )(implicit write: Write[K], ec: ExecutionContext): Future[String] = {
    lockApi.lock(LockRequest(name = write.write(name), lease)).map(_.key)
  }

  def unlock(key: String): Future[UnlockResponse] = {
    lockApi.unlock(UnlockRequest(key))
  }
}
