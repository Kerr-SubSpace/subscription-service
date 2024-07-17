package app.kerrlab.subspace

import app.kerrlab.subspace.ports.persistence.SubscriptionRepository
import app.kerrlab.subspace.ports.web.SubscriptionService
import io.kotest.core.spec.style.FunSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [Application::class])
class SubscriptionServiceTest : FunSpec() {

    @MockBean
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Autowired
    private lateinit var subservice: SubscriptionService

    init {
        test("Subscription can be created") {
            subscriptionRepository.findAll()
            println("kek")
        }
    }
}