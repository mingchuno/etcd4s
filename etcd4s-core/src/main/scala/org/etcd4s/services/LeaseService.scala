package org.etcd4s.services

import org.etcd4s.pb.etcdserverpb.LeaseGrpc.{Lease, LeaseStub}
import org.etcd4s.pb.etcdserverpb._
import io.grpc.stub.StreamObserver

private[etcd4s] class LeaseService(protected val stub: LeaseStub) extends Lease {

  override def leaseGrant(request: LeaseGrantRequest) = stub.leaseGrant(request)

  override def leaseRevoke(request: LeaseRevokeRequest) = stub.leaseRevoke(request)

  override def leaseKeepAlive(responseObserver: StreamObserver[LeaseKeepAliveResponse]) = stub.leaseKeepAlive(responseObserver)

  override def leaseTimeToLive(request: LeaseTimeToLiveRequest) = stub.leaseTimeToLive(request)

  override def leaseLeases(request: LeaseLeasesRequest) = stub.leaseLeases(request)

}
