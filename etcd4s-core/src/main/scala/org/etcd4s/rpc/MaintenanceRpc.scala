package org.etcd4s.rpc

import org.etcd4s.pb.etcdserverpb.MaintenanceGrpc.{Maintenance, MaintenanceStub}
import org.etcd4s.pb.etcdserverpb._
import io.grpc.stub.StreamObserver

import scala.concurrent.Future

private[etcd4s] class MaintenanceRpc(protected val stub: MaintenanceStub) extends Maintenance {
  override def alarm(request: AlarmRequest): Future[AlarmResponse] = stub.alarm(request)

  override def status(request: StatusRequest): Future[StatusResponse] = stub.status(request)

  override def defragment(request: DefragmentRequest): Future[DefragmentResponse] =
    stub.defragment(request)

  override def hash(request: HashRequest): Future[HashResponse] = stub.hash(request)

  override def hashKV(request: HashKVRequest): Future[HashKVResponse] = stub.hashKV(request)

  override def snapshot(
      request: SnapshotRequest,
      responseObserver: StreamObserver[SnapshotResponse]
  ): Unit =
    stub.snapshot(request, responseObserver)

  override def moveLeader(request: MoveLeaderRequest): Future[MoveLeaderResponse] =
    stub.moveLeader(request)
}
