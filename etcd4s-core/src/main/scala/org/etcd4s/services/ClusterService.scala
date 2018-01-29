package org.etcd4s.services

import org.etcd4s.pb.etcdserverpb.ClusterGrpc.{Cluster, ClusterStub}
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.{ExecutionContext, Future}

private[etcd4s] class ClusterService(protected val stub: ClusterStub) extends Cluster {
  override def memberAdd(request: MemberAddRequest): Future[MemberAddResponse] = {
    stub.memberAdd(request)
  }

  def memberAdd(peerURLs: Seq[String]): Future[MemberAddResponse] = {
    memberAdd(MemberAddRequest(peerURLs))
  }

  override def memberRemove(request: MemberRemoveRequest): Future[MemberRemoveResponse] = {
    stub.memberRemove(request)
  }

  def memberRemove(iD: Long)(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    memberRemove(MemberRemoveRequest(iD)).map(_.members)
  }

  override def memberUpdate(request: MemberUpdateRequest): Future[MemberUpdateResponse] = {
    stub.memberUpdate(request)
  }

  def memberUpdate(iD: Long, peerURLs: Seq[String])(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    memberUpdate(MemberUpdateRequest(iD, peerURLs)).map(_.members)
  }

  override def memberList(request: MemberListRequest): Future[MemberListResponse] = {
    stub.memberList(request)
  }

  def memberList()(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    memberList(MemberListRequest()).map(_.members)
  }
}
