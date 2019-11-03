gRPC Kotlin demo
================

Trying [gRPC](https://grpc.io) with [Kotlin](https://kotlinlang.org/). 

Inspiration:
* [gRPC Java quickstart guide](https://grpc.io/docs/quickstart/java.html)
* [Official gRPC Kotlin example](https://github.com/grpc/grpc-java/tree/master/examples/example-kotlin)

For best experience (as always :p), use IntelliJ IDEA and install [the Protobuf plugin](https://plugins.jetbrains.com/plugin/8277-protobuf-support/) (not required but it's nice).

To build the examples,

1. **[Install gRPC Java library SNAPSHOT locally, including code generation plugin](../../COMPILING.md) (Only need this step for non-released versions, e.g. master HEAD).**

2. Run in this directory:
```
$ ../gradlew installDist
```

This creates the scripts `hello-world-server`, `hello-world-client`,
`route-guide-server`, and `route-guide-client` in the
`build/install/examples/bin/` directory that run the examples. Each
example requires the server to be running before starting the client.

For example, to try the hello world example first run:

```
$ ./build/install/examples/bin/hello-world-server
```

And in a different terminal window run:

```
$ ./build/install/examples/bin/hello-world-client
```

That's it!

Please refer to gRPC Java's [README](../README.md) and
[tutorial](https://grpc.io/docs/tutorials/basic/java.html) for more
information.

Unit test examples
==============================================

Examples for unit testing gRPC clients and servers are located in [./src/test](./src/test).

In general, we DO NOT allow overriding the client stub.
We encourage users to leverage `InProcessTransport` as demonstrated in the examples to
write unit tests. `InProcessTransport` is light-weight and runs the server
and client in the same process without any socket/TCP connection.

For testing a gRPC client, create the client with a real stub
using an InProcessChannelBuilder.java and test it against an InProcessServer.java
with a mock/fake service implementation.

For testing a gRPC server, create the server as an InProcessServer,
and test it against a real client stub with an InProcessChannel.

The gRPC-java library also provides a JUnit rule, GrpcCleanupRule.java, to do the graceful shutdown
boilerplate for you.
