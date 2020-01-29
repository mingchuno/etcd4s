package org.etcd4s

import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.implicits._

class AuthServiceSpec extends Etcd4sFeatureSpec {

  override def beforeAll(): Unit = {
    // TODO: move to config
    val users = client.authRpc.userList(AuthUserListRequest()).futureValue.users
    if (!users.contains("root")) {
      client.authRpc
        .userAdd(AuthUserAddRequest(name = "root", password = "Admin123"))
        .futureValue
      client.authRpc.userGrantRole(AuthUserGrantRoleRequest("root", "root")).futureValue
    }
    client.authRpc.authEnable(AuthEnableRequest()).futureValue
  }

  feature("client with auth") {
    scenario("should be able the call api") {
      val authClient = getAuthClient
      authClient.kvRpc.put(PutRequest().withKey("foo").withValue("bar")).futureValue
      val value: String =
        authClient.kvRpc.range(RangeRequest().withKey("foo")).futureValue.kvs.head.value
      info(s"value is:$value")
      value shouldBe "bar"

      authClient.authRpc.authDisable(AuthDisableRequest()).futureValue
      authClient.shutdown()
    }
  }
}
