package org.etcd4s.rpc

import org.etcd4s.pb.v3lockpb.LockGrpc.{Lock, LockStub}
import org.etcd4s.pb.v3lockpb._

import scala.concurrent.Future

private[etcd4s] class LockRpc(protected val stub: LockStub) extends Lock {

  override def lock(request: LockRequest): Future[LockResponse] = stub.lock(request)

  override def unlock(request: UnlockRequest): Future[UnlockResponse] = stub.unlock(request)

}
