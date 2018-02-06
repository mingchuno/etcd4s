package org.etcd4s.rpc

import org.etcd4s.pb.v3lockpb.LockGrpc.{Lock, LockStub}
import org.etcd4s.pb.v3lockpb._

private[etcd4s] class LockRpc(protected val stub: LockStub) extends Lock {

  override def lock(request: LockRequest) = stub.lock(request)

  override def unlock(request: UnlockRequest) = stub.unlock(request)

}
