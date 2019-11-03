package nu.westlin.grpc.users

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

// TODO petves: Refact
/**
 * A simple client that requests a greeting from the [UsersServer].
 */
class UsersClient
/** Construct client for accessing RouteGuide server using the existing channel.  */
internal constructor(private val channel: ManagedChannel) {
    private val userBlockingStub: UserGrpc.UserBlockingStub = UserGrpc.newBlockingStub(channel)

    /** Construct client connecting to HelloWorld server at `host:port`.  */
    constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext()
        .build()) {
    }


    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    fun getUser(id: String) {
        logger.log(Level.INFO, "Will try to get user ${id}...")
        val request = UserRequest.newBuilder().setId(id).build()
        val response: UserResponse = try {
            userBlockingStub.getUser(request)
        } catch (e: StatusRuntimeException) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.status)
            return
        }

        logger.info("User: ${response.message}")
    }


    companion object {
        private val logger = Logger.getLogger(UsersClient::class.java.name)

        /**
         * Greet server. If provided, the first element of `args` is the name to use in the
         * greeting.
         */
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val client = UsersClient("localhost", 50051)
            try {
                /* Access a service running on the local machine on port 50051 */
                val userId = if (args.isNotEmpty()) args[0] else "Foo"
                client.getUser(userId)
            } finally {
                client.shutdown()
            }
        }
    }
}
