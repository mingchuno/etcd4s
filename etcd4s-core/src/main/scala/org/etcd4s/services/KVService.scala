package org.etcd4s.services

import org.etcd4s.pb.etcdserverpb.KVGrpc.{KV, KVStub}
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.Future

private[etcd4s] class KVService(protected val stub: KVStub) extends KV {

  override def range(request: RangeRequest): Future[RangeResponse] = {
    stub.range(request)
  }

  override def put(request: PutRequest): Future[PutResponse] = {
    stub.put(request)
  }

  override def deleteRange(request: DeleteRangeRequest): Future[DeleteRangeResponse] = {
    stub.deleteRange(request)
  }

  override def txn(request: TxnRequest): Future[TxnResponse] = {
    stub.txn(request)
  }

  override def compact(request: CompactionRequest): Future[CompactionResponse] = {
    stub.compact(request)
  }
}
