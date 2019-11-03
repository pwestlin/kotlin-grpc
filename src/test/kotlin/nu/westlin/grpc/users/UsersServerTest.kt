package nu.westlin.grpc.users

import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UsersServerTest {

    /**
     * Graceful shutdown for the registered components (servers and channels).
     */
    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    @Test
    @Throws(Exception::class)
    fun greeterImpl_replyMessage() {
        val serverName = InProcessServerBuilder.generateName()

        grpcCleanup.register(InProcessServerBuilder
            .forName(serverName).directExecutor().addService(UsersServer.UserImpl()).build().start())

        val blockingStub = UserGrpc.newBlockingStub(
            grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor()
                .build()))


        val reply = blockingStub.getUser(UserRequest.newBuilder().setId("1").build())

        assertEquals("User with id 1", reply.message)
    }
}
