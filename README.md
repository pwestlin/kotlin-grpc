gRPC Kotlin demo
================

Trying [gRPC](https://grpc.io) with [Kotlin](https://kotlinlang.org/). 

Inspiration:
* [gRPC Java quickstart guide](https://grpc.io/docs/quickstart/java.html)
* [Official gRPC Kotlin example](https://github.com/grpc/grpc-java/tree/master/examples/example-kotlin)

For best experience (as always :p), use IntelliJ IDEA and install [the Protobuf plugin](https://plugins.jetbrains.com/plugin/8277-protobuf-support/) (not required but it's nice).

To build the examples,

1. ```./gradlew installDist```

2. ```./build/install/kotlin-grpc/bin/users-server.sh```

3. ```./build/install/kotlin-grpc/bin/users-client.sh```