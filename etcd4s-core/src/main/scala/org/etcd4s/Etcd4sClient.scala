package org.etcd4s

import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.services._
import io.grpc.ManagedChannel
import org.etcd4s.pb.v3electionpb.ElectionGrpc
import org.etcd4s.pb.v3lockpb.LockGrpc

class Etcd4sClient(val channel: ManagedChannel) {
  val kvService = new KVService(KVGrpc.stub(channel))
  val clusterService = new ClusterService(ClusterGrpc.stub(channel))
  val authService = new AuthService(AuthGrpc.stub(channel))
  val leaseService = new LeaseService(LeaseGrpc.stub(channel))
  val watchService = new WatchService(WatchGrpc.stub(channel))
  val maintenanceService = new MaintenanceService(MaintenanceGrpc.stub(channel))
  val lockService = new LockService(LockGrpc.stub(channel))
  val electionService = new ElectionService(ElectionGrpc.stub(channel))
}
