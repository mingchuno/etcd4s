# etcd4s

[![Build Status](https://travis-ci.org/mingchuno/etcd4s.svg?branch=master)](https://travis-ci.org/mingchuno/etcd4s)

A Scala etcd client implementing V3 API using gRPC and ScalaPB with optional Akka Stream support. This project is in beta stage with basic test coverage and usable APIs.

## Overview

This repo is a client library of [etcd](https://etcd.io/) implementing V3 [APIs](https://etcd.io/docs/v3.3.12/rfc/) using gRPC under the hood with optional Akka Stream support for stream APIs. This library implement the complete set of the APIs in the V3 protoal. More information about the APIs can be found here:

* [etcd V3 API overview](https://etcd.io/docs/v3.3.12/rfc/)
* [etcd V3 API Reference](https://etcd.io/docs/v3.3.12/dev-guide/api_reference_v3/)
* [protobuf defination](https://github.com/mingchuno/etcd4s/tree/master/etcd4s-core/src/main/protobuf)

Note that this library do not support gRPC json gateway and use raw gRPC call instead (underlying is java-grpc). This project cross build against Scala 2.11, 2.12 and 2.13 and also tested against etcd 3.2.x, 3.3.x but fail under 3.4.x.

## Getting Started

The core lib

```scala
libraryDependencies += "com.github.mingchuno" %% "etcd4s-core" % "0.3.0"
```

To include akka stream support for stream API

```scala
libraryDependencies += "com.github.mingchuno" %% "etcd4s-akka-stream" % "0.3.0"
```

## Usage

```scala
import org.etcd4s.{Etcd4sClientConfig, Etcd4sClient}
import org.etcd4s.implicits._
import org.etcd4s.formats._
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.ExecutionContext.Implicits.global

// create the client
val config = Etcd4sClientConfig(
  address = "127.0.0.1",
  port = 2379
)
val client = Etcd4sClient.newClient(config)

// set a key
client.setKey("foo", "bar") // return a Future

// get a key
client.getKey("foo").foreach { result =>
  assert(result == Some("bar"))
}

// delete a key
client.deleteKey("foo").foreach { result =>
  assert(result == 1)
}

// set more key
client.setKey("foo/bar", "Hello")
client.setKey("foo/baz", "World")

// get keys with range
client.getRange("foo/").foreach { result =>
  assert(result.count == 2)
}

// remember to shutdown the client
client.shutdown()
```

The above is wrapper for simplified APIs. If you want to access all underlying APIs. You can use the corresponding class to have more control

```scala
client.kvApi.range(...)
client.kvApi.put(...)
client.leaseApi.leaseGrant(...)
client.electionApi.leader(...)
```

If you want the Akka Stream support for the stream APIs, you should add the `etcd4s-akka-stream` depns into your `build.sbt`

```scala
import org.etcd4s.akkasupport._
import org.etcd4s.implicits._
import org.etcd4s.pb.etcdserverpb._
import akka.NotUsed

// assume you have the implicit value and client needed in the scope
val flow: Flow[WatchRequest, WatchResponse, NotUsed] = client.watchApi.watchFlow
val request: WatchRequest = WatchRequest().withCreateRequest(WatchCreateRequest().withKey("foo"))
Source.single(request)
  .via(flow)
  .runForeach { resp =>
    println(resp)
  }
```

More example usage under the test dir in the repo.

## Development

### Requirment

* Java 8+, Scala 12.12.X+, sbt and docker

```bash
# to start a background etcd for development
docker-compose up -d
```

### How to start?

Simple! Just `sbt test`

### Publish

This is to remind me how to publish and may switch to `sbt-release` later

1. make sure you have `~/.sbt/gpg/` ready with pub/sec key paris
2. make sure you have `~/.sbt/1.0/sonatype.sbt` ready with credentials
3. `sbt "+clean" "+compile"`
4. `sbt "+publishSigned"`
5. `sbt sonatypeReleaseAll`
