package org.etcd4s

import org.etcd4s.formats.Formats._

import scala.concurrent.ExecutionContext.Implicits.global

class AuthServiceSpec extends Etcd4sFeatureSpec {

  feature("client with auth") {
    scenario("should be able the call api") {
      authClient.kvService.setKey("foo", "bar").futureValue
      val value: String = authClient.kvService.getKey("foo").futureValue
      info(s"value is:$value")
      value shouldBe "bar"
    }
  }
}
