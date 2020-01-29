package org.etcd4s.rpc

import org.etcd4s.pb.etcdserverpb.LeaseGrpc.{Lease, LeaseStub}
import org.etcd4s.pb.etcdserverpb._
import io.grpc.stub.StreamObserver

import scala.concurrent.Future

private[etcd4s] class LeaseRpc(protected val stub: LeaseStub) extends Lease {

  override def leaseGrant(request: LeaseGrantRequest): Future[LeaseGrantResponse] =
    stub.leaseGrant(request)

  override def leaseRevoke(request: LeaseRevokeRequest): Future[LeaseRevokeResponse] =
    stub.leaseRevoke(request)

  override def leaseKeepAlive(
      responseObserver: StreamObserver[LeaseKeepAliveResponse]
  ): StreamObserver[LeaseKeepAliveRequest] =
    stub.leaseKeepAlive(responseObserver)

  override def leaseTimeToLive(request: LeaseTimeToLiveRequest): Future[LeaseTimeToLiveResponse] =
    stub.leaseTimeToLive(request)

  override def leaseLeases(request: LeaseLeasesRequest): Future[LeaseLeasesResponse] =
    stub.leaseLeases(request)

}
