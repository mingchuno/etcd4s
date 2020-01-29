package org.etcd4s.services

import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.rpc.ClusterRpc

import scala.concurrent.{ExecutionContext, Future}

trait ClusterService {
  val clusterApi: ClusterRpc
  def memberAdd(peerURLs: Seq[String]): Future[MemberAddResponse] = {
    clusterApi.memberAdd(MemberAddRequest(peerURLs))
  }

  def memberRemove(iD: Long)(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    clusterApi.memberRemove(MemberRemoveRequest(iD)).map(_.members)
  }

  def memberUpdate(iD: Long, peerURLs: Seq[String])(
      implicit ec: ExecutionContext
  ): Future[Seq[Member]] = {
    clusterApi.memberUpdate(MemberUpdateRequest(iD, peerURLs)).map(_.members)
  }

  def memberList()(implicit ec: ExecutionContext): Future[Seq[Member]] = {
    clusterApi.memberList(MemberListRequest()).map(_.members)
  }
}
