package org.etcd4s

import org.etcd4s.formats.Formats._

import scala.concurrent.ExecutionContext.Implicits.global

class AuthServiceSpec extends Etcd4sFeatureSpec {

  override def beforeAll(): Unit = {
    // TODO: move to config
    val users = client.authService.userList().futureValue
    if (!users.contains("root")) {
      client.authService.userAdd("root", "Admin123").futureValue
      client.authService.userGrantRole("root", "root").futureValue
    }
    client.authService.authEnable().futureValue
  }

  feature("client with auth") {
    scenario("should be able the call api") {
      val authClient = getAuthClient
      authClient.kvService.setKey("foo", "bar").futureValue
      val value: String = authClient.kvService.getKey("foo").futureValue.get
      info(s"value is:$value")
      value shouldBe "bar"

      authClient.authService.authDisable().futureValue
      authClient.shutdown()
    }
  }
}
