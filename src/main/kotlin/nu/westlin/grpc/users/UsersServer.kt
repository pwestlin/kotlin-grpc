package nu.westlin.grpc.users

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

// TODO petves: Refact
class UsersServer {

    private var server: Server? = null

    @Throws(IOException::class)
    private fun start() {
        val port = 8080
        server = ServerBuilder.forPort(port)
            .addService(UserImpl())
            .build()
            .start()
        logger.log(Level.INFO, "Server started on port $port")

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                System.err.println("shutting down server...")
                this@UsersServer.stop()
                System.err.println("server shut down")
            }
        })
    }

    private fun stop() {
        server?.shutdown()
    }

    @Throws(InterruptedException::class)
    private fun blockUntilShutdown() {
        server?.awaitTermination()
    }

    internal class UserImpl : UserGrpc.UserImplBase() {

        override fun getUser(req: UserRequest, responseObserver: StreamObserver<UserResponse>) {
            val reply = UserResponse.newBuilder().setMessage("User with id ${req.id}").build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    companion object {
        private val logger = Logger.getLogger(UsersServer::class.java.name)

        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val server = UsersServer()
            server.start()
            server.blockUntilShutdown()
        }
    }
}
