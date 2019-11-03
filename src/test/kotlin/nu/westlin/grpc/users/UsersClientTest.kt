package nu.westlin.grpc.users


import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.StreamObserver
import io.grpc.testing.GrpcCleanupRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.AdditionalAnswers.delegatesTo
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Unit tests for [UsersClient].
 * For demonstrating how to write gRPC unit test only.
 * Not intended to provide a high code coverage or to test every major usecase.
 *
 *
 * For more unit test examples see [io.grpc.examples.routeguide.RouteGuideClientTest] and
 * [io.grpc.examples.routeguide.RouteGuideServerTest].
 */
@RunWith(JUnit4::class)
class UsersClientTest {
    /**
     * This rule manages automatic graceful shutdown for the registered servers and channels at the
     * end of test.
     */
    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    private val serviceImpl = mock(UserGrpc.UserImplBase::class.java, delegatesTo<Any>(
        object : UserGrpc.UserImplBase() {
            // By default the client will receive Status.UNIMPLEMENTED for all RPCs.
            // You might need to implement necessary behaviors for your test here, like this:
            //
            // override fun sayHello(req: HelloRequest, respObserver: StreamObserver<HelloReply>) {
            //     respObserver.onNext(HelloReply.getDefaultInstance())
            //     respObserver.onCompleted()
        }))

    private var client: UsersClient? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        // Generate a unique in-process server name.
        val serverName = InProcessServerBuilder.generateName()

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
            .forName(serverName).directExecutor().addService(serviceImpl).build().start())

        // Create a client channel and register for automatic graceful shutdown.
        val channel = grpcCleanup.register(
            InProcessChannelBuilder.forName(serverName).directExecutor().build())

        // Create a HelloWorldClient using the in-process channel;
        client = UsersClient(channel)
    }

    /**
     * To test the client, call from the client against the fake server, and verify behaviors or state
     * changes from the server side.
     */
    @Test
    fun greet_messageDeliveredToServer() {
        val requestCaptor = ArgumentCaptor.forClass(UserRequest::class.java)

        client!!.getUser("1")

        verify<UserGrpc.UserImplBase>(serviceImpl)
            .getUser(requestCaptor.capture(), ArgumentMatchers.any<StreamObserver<UserResponse>>())
        assertEquals("1", requestCaptor.value.id)
    }
}
