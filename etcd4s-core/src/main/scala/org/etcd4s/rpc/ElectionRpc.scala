package org.etcd4s.rpc

import io.grpc.stub.StreamObserver
import org.etcd4s.pb.v3electionpb.ElectionGrpc.{Election, ElectionStub}
import org.etcd4s.pb.v3electionpb._

import scala.concurrent.Future

private[etcd4s] class ElectionRpc(protected val stub: ElectionStub) extends Election  {
  override def campaign(request: CampaignRequest): Future[CampaignResponse] =
    stub.campaign(request)

  override def proclaim(request: ProclaimRequest): Future[ProclaimResponse] =
    stub.proclaim(request)

  override def leader(request: LeaderRequest): Future[LeaderResponse] =
    stub.leader(request)

  override def observe(request: LeaderRequest, responseObserver: StreamObserver[LeaderResponse]): Unit =
    stub.observe(request, responseObserver)

  override def resign(request: ResignRequest): Future[ResignResponse] =
    stub.resign(request)
}
