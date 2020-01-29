package org.etcd4s

import java.util.concurrent.TimeUnit

import io.grpc._
import io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.MetadataUtils
import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.pb.v3electionpb.ElectionGrpc
import org.etcd4s.pb.v3lockpb.LockGrpc
import org.etcd4s.rpc._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

private[etcd4s] class EtcdChannelBuilder(config: Etcd4sClientConfig) {
  def build()(implicit ec: ExecutionContext): ManagedChannel = {
    val builder = NettyChannelBuilder.forAddress(config.address, config.port)
    if (config.sslContext.isDefined) {
      builder.sslContext(config.sslContext.get)
    } else {
      builder.usePlaintext()
    }
    if (config.credential.isDefined) {
      val tempChannel = builder.build()
      val authRpc = new AuthRpc(AuthGrpc.stub(tempChannel))
      val username = config.credential.get.user
      val password = config.credential.get.password
      val f =
        authRpc.authenticate(AuthenticateRequest(name = username, password = password)).map(_.token)
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

private[etcd4s] class Etcd4sRpcClient(val channel: ManagedChannel) {
  val kvApi = new KVRpc(KVGrpc.stub(channel))
  val clusterApi = new ClusterRpc(ClusterGrpc.stub(channel))
  val authApi = new AuthRpc(AuthGrpc.stub(channel))
  val leaseApi = new LeaseRpc(LeaseGrpc.stub(channel))
  val watchApi = new WatchRpc(WatchGrpc.stub(channel))
  val maintenanceApi = new MaintenanceRpc(MaintenanceGrpc.stub(channel))
  val lockApi = new LockRpc(LockGrpc.stub(channel))
  val electionApi = new ElectionRpc(ElectionGrpc.stub(channel))

  def shutdown(): Boolean = {
    channel.shutdown()
    channel.awaitTermination(5, TimeUnit.SECONDS)
  }
}

object Etcd4sRpcClient {
  def newClient(config: Etcd4sClientConfig)(implicit ec: ExecutionContext): Etcd4sRpcClient = {
    new Etcd4sRpcClient(new EtcdChannelBuilder(config).build())
  }
}
