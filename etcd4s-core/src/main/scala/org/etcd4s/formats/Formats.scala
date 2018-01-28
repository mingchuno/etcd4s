package org.etcd4s.formats

import com.google.protobuf.ByteString
import org.etcd4s.implicits._

trait Read[T] {
  def read(value: ByteString): T
}

object DefaultReaders extends DefaultReaders
trait DefaultReaders {
  implicit object StringReader extends Read[String] {
    override def read(value: ByteString): String = {
      decode(value)
    }
  }
}

trait Write[T] {
  def write(value: T): ByteString
}

object DefaultWriters extends DefaultWriters
trait DefaultWriters {
  implicit object StringWriter extends Write[String] {
    override def write(value: String): ByteString = {
      encode(value)
    }
  }
}

object Formats extends DefaultWriters with DefaultReaders