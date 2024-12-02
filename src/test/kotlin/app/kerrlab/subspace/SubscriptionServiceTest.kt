package app.kerrlab.subspace

import app.kerrlab.subspace.ports.persistence.SubscriptionRepository
import app.kerrlab.subspace.ports.web.SubscriptionService
import app.kerrlab.subspace.util.randomSubscriptionRequest
import arrow.core.right
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [Application::class])
class SubscriptionServiceTest : FunSpec() {

    @Autowired
    private lateinit var subservice: SubscriptionService

    init {
        test("Subscription can be created") {
            val randomSubscriptionRequest = randomSubscriptionRequest()
            println(randomSubscriptionRequest)
        }
    }
}