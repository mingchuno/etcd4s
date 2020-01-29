package org.etcd4s.services

import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.rpc.LeaseRpc

import scala.concurrent.{ExecutionContext, Future}

trait LeaseService {
  val leaseApi: LeaseRpc
  def leaseGrant(ttl: Long)(implicit ec: ExecutionContext): Future[Long] = {
    leaseApi.leaseGrant(LeaseGrantRequest(tTL = ttl)).map(_.iD)
  }

  def leaseRevoke(id: Long): Future[LeaseRevokeResponse] = {
    leaseApi.leaseRevoke(LeaseRevokeRequest(id))
  }

  def leaseLeases()(implicit ec: ExecutionContext): Future[Seq[LeaseStatus]] = {
    leaseApi.leaseLeases(LeaseLeasesRequest()).map(_.leases)
  }
}
