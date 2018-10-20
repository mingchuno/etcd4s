package org.etcd4s.services

import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.rpc.ClusterRpc

import scala.concurrent.{ExecutionContext, Future}

private[etcd4s] class ClusterService(protected val clusterRpc: ClusterRpc) {
  def memberAdd(peerURLs: Seq[String]): Future[MemberAddResponse] = {
    clusterRpc.memberAdd(MemberAddRequest(peerURLs))
  }

  def memberRemove(iD: Long)(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    clusterRpc.memberRemove(MemberRemoveRequest(iD)).map(_.members)
  }

  def memberUpdate(iD: Long, peerURLs: Seq[String])(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    clusterRpc.memberUpdate(MemberUpdateRequest(iD, peerURLs)).map(_.members)
  }

  def memberList()(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    clusterRpc.memberList(MemberListRequest()).map(_.members)
  }
}
