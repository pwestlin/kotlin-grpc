package nu.westlin.grpc.users

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class UsersClient internal constructor(private val channel: ManagedChannel) : AutoCloseable {

    private val userBlockingStub: UserGrpc.UserBlockingStub = UserGrpc.newBlockingStub(channel)

    private constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build())

    @Throws(InterruptedException::class)
    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    fun getUser(id: String) {
        logger.log(Level.INFO, "Looking for user with id ${id}...")
        val request = UserRequest.newBuilder().setId(id).build()
        val response: UserResponse = try {
            userBlockingStub.getUser(request)
        } catch (e: StatusRuntimeException) {
            logger.log(Level.WARNING, "gRPC failure: ${e.status}")
            return
        }

        logger.info("User: ${response.message}")
    }


    companion object {
        private val logger = Logger.getLogger(UsersClient::class.java.name)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            UsersClient("localhost", 8080).use { it.getUser(if (args.isNotEmpty()) args[0] else "Foo") }
        }
    }
}
