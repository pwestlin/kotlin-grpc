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

@RunWith(JUnit4::class)
class UsersClientTest {

    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    private val serviceImpl = mock(UserServiceGrpc.UserServiceImplBase::class.java, delegatesTo<Any>(
        object : UserServiceGrpc.UserServiceImplBase() {
            override fun getUser(request: UserRequest, responseObserver: StreamObserver<UserResponse>) {
                responseObserver.onNext(UserResponse.getDefaultInstance())
                responseObserver.onCompleted()
            }
        }))

    private var client: UsersClient? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val serverName = InProcessServerBuilder.generateName()

        grpcCleanup.register(InProcessServerBuilder.forName(serverName).directExecutor().addService(serviceImpl).build().start())

        val channel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
        client = UsersClient(channel)
    }

    @Test
    fun greet_messageDeliveredToServer() {
        val requestCaptor = ArgumentCaptor.forClass(UserRequest::class.java)

        client!!.getUser("1")

        verify<UserServiceGrpc.UserServiceImplBase>(serviceImpl)
            .getUser(requestCaptor.capture(), ArgumentMatchers.any<StreamObserver<UserResponse>>())
        assertEquals("1", requestCaptor.value.id)
    }
}
