package org.etcd4s.services

import org.etcd4s.formats.{Read, Write}
import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.pb.mvccpb.KeyValue
import org.etcd4s.implicits._
import org.etcd4s.rpc.KVRpc

import scala.concurrent.{ExecutionContext, Future}

trait KVService {
  val kvApi: KVRpc
  def getKey[K, V](
      key: K
  )(implicit read: Read[V], write: Write[K], ec: ExecutionContext): Future[Option[V]] = {
    kvApi
      .range(RangeRequest().withKey(write.write(key)))
      .map(_.kvs)
      .map { kvs =>
        if (kvs.isEmpty) None else Some(read.read(kvs.head.value))
      }
  }

  def getRange[K](key: K)(implicit write: Write[K]): Future[RangeResponse] = {
    kvApi.range(RangeRequest().withPrefix(write.write(key)))
  }

  def setKey[K, V](
      key: K,
      value: V
  )(implicit writeK: Write[K], writeV: Write[V], ec: ExecutionContext): Future[Option[KeyValue]] = {
    kvApi
      .put(PutRequest(key = writeK.write(key), value = writeV.write(value)))
      .map(_.prevKv)
  }

  def deleteKey[K](key: K)(implicit write: Write[K], ec: ExecutionContext): Future[Long] = {
    kvApi.deleteRange(DeleteRangeRequest(key = write.write(key))).map(_.deleted)
  }

  def deleteRange[K](key: K)(implicit write: Write[K], ec: ExecutionContext): Future[Long] = {
    kvApi.deleteRange(DeleteRangeRequest().withPrefix(write.write(key))).map(_.deleted)
  }

  def txn(request: TxnRequest): Future[TxnResponse] = {
    kvApi.txn(request)
  }

  def compact(revision: Long, physical: Boolean): Future[CompactionResponse] = {
    kvApi.compact(CompactionRequest(revision, physical))
  }
}
