package org.etcd4s

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import org.etcd4s.akkasupport.GrpcAkkaStreams.{getBidiFlow, getServerStreamingFLow}
import org.etcd4s.pb.etcdserverpb.LeaseGrpc.Lease
import org.etcd4s.pb.etcdserverpb.MaintenanceGrpc.Maintenance
import org.etcd4s.pb.etcdserverpb.WatchGrpc.Watch
import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.pb.v3electionpb.ElectionGrpc.Election
import org.etcd4s.pb.v3electionpb.{LeaderRequest, LeaderResponse}

package object akkasupport {

  implicit class LeaseServiceWithAkkaSupport(lease: Lease) {
    val leaseKeepAliveFlow: Flow[LeaseKeepAliveRequest, LeaseKeepAliveResponse, NotUsed] =
      getBidiFlow(lease.leaseKeepAlive)
  }

  implicit class MaintenanceServiceWithAkkaSupport(maintenance: Maintenance) {
    def snapshotSource(request: SnapshotRequest): Source[SnapshotResponse, NotUsed] =
      getServerStreamingFLow(request)(maintenance.snapshot)
  }

  implicit class WatchServiceWithAkkaSupport(watch: Watch) {
    val watchFlow: Flow[WatchRequest, WatchResponse, NotUsed] =
      getBidiFlow(watch.watch)
  }

  implicit class ElectionServiceAkkaSupport(election: Election) {
    def observe(request: LeaderRequest): Source[LeaderResponse, NotUsed] =
      getServerStreamingFLow(request)(election.observe)
  }

}
