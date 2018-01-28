package org.etcd4s

import io.netty.handler.ssl.SslContext

case class Credential(user: String, password: String)

case class Etcd4sClientConfig(address: String,
                              port: Int,
                              credential: Option[Credential] = None,
                              sslContext: Option[SslContext] = None) {
  def withCredential(username: String, password: String): Etcd4sClientConfig = {
    require(username != null)
    require(password != null)
    this.copy(credential = Some(Credential(user = username, password = password)))
  }

  def withSslContext(sslContext: SslContext): Etcd4sClientConfig = {
    require(sslContext != null)
    this.copy(sslContext = Option(sslContext))
  }
}
