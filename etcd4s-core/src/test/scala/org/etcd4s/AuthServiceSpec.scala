package org.etcd4s

import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.implicits._

class AuthServiceSpec extends Etcd4sFeatureSpec {

  override def beforeAll(): Unit = {
    // TODO: move to config
    val users = client.authApi.userList(AuthUserListRequest()).futureValue.users
    if (!users.contains("root")) {
      client.authApi
        .userAdd(AuthUserAddRequest(name = "root", password = "Admin123"))
        .futureValue
      client.authApi.userGrantRole(AuthUserGrantRoleRequest("root", "root")).futureValue
    }
    client.authApi.authEnable(AuthEnableRequest()).futureValue
  }

  Feature("client with auth") {
    Scenario("should be able the call api") {
      val authClient = getAuthClient
      authClient.kvApi.put(PutRequest().withKey("foo").withValue("bar")).futureValue
      val value: String =
        authClient.kvApi.range(RangeRequest().withKey("foo")).futureValue.kvs.head.value
      info(s"value is:$value")
      value shouldBe "bar"

      authClient.authApi.authDisable(AuthDisableRequest()).futureValue
      authClient.shutdown()
    }
  }
}
