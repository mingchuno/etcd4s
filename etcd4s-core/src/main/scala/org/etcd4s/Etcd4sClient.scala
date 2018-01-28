package org.etcd4s

import io.grpc._
import io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.MetadataUtils
import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.pb.v3electionpb.ElectionGrpc
import org.etcd4s.pb.v3lockpb.LockGrpc
import org.etcd4s.services._

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

private[etcd4s] class EtcdChannelBuilder(config: Etcd4sClientConfig) {
  def build()(implicit ec: ExecutionContext): ManagedChannel = {
    val builder = NettyChannelBuilder.forAddress(config.address, config.port)
    if (config.sslContext.isDefined) {
      builder.sslContext(config.sslContext.get)
    } else {
      builder.usePlaintext(true)
    }
    if (config.credential.isDefined) {
      val tempChannel = builder.build()
      val authService = new AuthService(AuthGrpc.stub(tempChannel))
      val username = config.credential.get.user
      val password = config.credential.get.password
      val f = authService.authenticate(name = username, password = password)
      val token = Await.result(f, 10 seconds)
      builder.intercept(authRequestInterceptor(token))
    }
    builder.build()
  }

  /**
    * return the ClientInterceptor which inject the auth token inside
    * @param token the auth token
    * @return a ClientInterceptor
    */
  private def authRequestInterceptor(token: String): ClientInterceptor = {
    val TOKEN = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER)
    val headers = new Metadata
    headers.put(TOKEN, token)
    MetadataUtils.newAttachHeadersInterceptor(headers)
  }
}

private[etcd4s] class Etcd4sClient(val channel: ManagedChannel) {
  val kvService = new KVService(KVGrpc.stub(channel))
  val clusterService = new ClusterService(ClusterGrpc.stub(channel))
  val authService = new AuthService(AuthGrpc.stub(channel))
  val leaseService = new LeaseService(LeaseGrpc.stub(channel))
  val watchService = new WatchService(WatchGrpc.stub(channel))
  val maintenanceService = new MaintenanceService(MaintenanceGrpc.stub(channel))
  val lockService = new LockService(LockGrpc.stub(channel))
  val electionService = new ElectionService(ElectionGrpc.stub(channel))
}

object Etcd4sClient {
  def newClient(config: Etcd4sClientConfig)(implicit ec: ExecutionContext): Etcd4sClient = {
    new Etcd4sClient(new EtcdChannelBuilder(config).build())
  }
}
