package org.etcd4s.services

import org.etcd4s.formats._
import org.etcd4s.implicits._
import org.etcd4s.pb.etcdserverpb.KVGrpc.{KV, KVStub}
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.{ExecutionContext, Future}

private[etcd4s] class KVService(protected val stub: KVStub) extends KV {

  def getKey[K,V](key: K)(implicit read: Read[V], write: Write[K], ec: ExecutionContext): Future[V] = {
    range(RangeRequest().withKey(write.write(key)))
      .map(_.kvs.head.value) // NoSuchElement
      .map(read.read)
  }

  def getRange[K](key: K)(implicit write: Write[K], ec: ExecutionContext): Future[RangeResponse] = {
    range(RangeRequest().withPrefix(write.write(key)))
  }

  def setKey[K,V](key: K, value: V)(implicit writeK: Write[K], writeV: Write[V], ec: ExecutionContext): Future[PutResponse] = {
    put(PutRequest(key = writeK.write(key), value = writeV.write(value)))
  }

  def deleteKey[K](key: K)(implicit write: Write[K], ec: ExecutionContext): Future[Long] = {
    deleteRange(DeleteRangeRequest(key = write.write(key))).map(_.deleted)
  }

  def deleteRange[K](key: K)(implicit write: Write[K], ec: ExecutionContext): Future[Long] = {
    deleteRange(DeleteRangeRequest().withPrefix(write.write(key))).map(_.deleted)
  }

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

  def compact(revision: Long, physical: Boolean): Future[CompactionResponse] = {
    compact(CompactionRequest(revision, physical))
  }
}
