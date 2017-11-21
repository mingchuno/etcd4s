package org.etcd4s

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Keep
import akka.stream.testkit.scaladsl.{TestSink, TestSource}
import akka.testkit.TestKit
import org.etcd4s.akkasupport._
import org.etcd4s.implicits._
import org.etcd4s.pb.etcdserverpb._
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.duration._

class WatchServiceSpec extends TestKit(ActorSystem("WatchServiceSpec")) with Etcd4sFeatureSpec with BeforeAndAfterAll {

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  implicit val ec = system.dispatcher
  implicit val mat = ActorMaterializer()

  feature("watch service") {
    scenario("watch for key change") {
      val KEY = "foo"
      val timeout = 3 seconds

      info("delete key")
      client.kvService.deleteRange(DeleteRangeRequest().withKey(KEY)).futureValue

      info("make the stream")
      val flowUnderTest = client.watchService.watchFlow
      val (pub, sub) = TestSource.probe[WatchRequest]
        .via(flowUnderTest)
        .toMat(TestSink.probe[WatchResponse])(Keep.both)
        .run()

      info("request watch")
      sub.request(n = 1)
      sub.expectNoMessage(timeout)
      pub.sendNext(WatchRequest().withCreateRequest(WatchCreateRequest().withKey(KEY)))
      val resp = sub.expectNextN(1).head
      resp.created shouldBe true
      val watchId = resp.watchId

      info("change some key")
      sub.request(n = 3)
      client.kvService.put(PutRequest().withKey(KEY).withValue("bar1")).futureValue
      client.kvService.put(PutRequest().withKey(KEY).withValue("bar2")).futureValue
      client.kvService.put(PutRequest().withKey(KEY).withValue("bar3")).futureValue

      info("check receive event")
      sub.expectNextN(3).map(_.events.head.kv.get.value).toList.map(x => x: String) shouldBe List("bar1", "bar2", "bar3")

      info("cancel watch")
      sub.request(n = 1)
      sub.expectNoMessage(timeout)
      pub.sendNext(WatchRequest().withCancelRequest(WatchCancelRequest(watchId = watchId)))
      sub.expectNextN(1).head.canceled shouldBe true
    }
  }

}
