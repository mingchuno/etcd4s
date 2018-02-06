package org.etcd4s.rpc

import org.etcd4s.pb.etcdserverpb.ClusterGrpc.{Cluster, ClusterStub}
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.Future

private[etcd4s] class ClusterRpc(protected val stub: ClusterStub) extends Cluster {
  override def memberAdd(request: MemberAddRequest): Future[MemberAddResponse] = {
    stub.memberAdd(request)
  }

  override def memberRemove(request: MemberRemoveRequest): Future[MemberRemoveResponse] = {
    stub.memberRemove(request)
  }

  override def memberUpdate(request: MemberUpdateRequest): Future[MemberUpdateResponse] = {
    stub.memberUpdate(request)
  }

  override def memberList(request: MemberListRequest): Future[MemberListResponse] = {
    stub.memberList(request)
  }

}
