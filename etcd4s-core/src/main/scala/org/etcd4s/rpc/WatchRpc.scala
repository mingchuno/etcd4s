package org.etcd4s.rpc

import org.etcd4s.pb.etcdserverpb.WatchGrpc.{Watch, WatchStub}
import org.etcd4s.pb.etcdserverpb._
import io.grpc.stub.StreamObserver

private[etcd4s] class WatchRpc(protected val stub: WatchStub) extends Watch {
  override def watch(responseObserver: StreamObserver[WatchResponse]) = stub.watch(responseObserver)
}
