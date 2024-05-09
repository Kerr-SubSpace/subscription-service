package app.kerrlab.subspace

import app.kerrlab.subspace.adapters.persistence.repository.SubscriptionRepository
import app.kerrlab.subspace.adapters.web.model.SubscriptionResponseDto
import app.kerrlab.subspace.application.SubscriptionServiceImpl
import app.kerrlab.subspace.domain.model.SubscriptionData
import app.kerrlab.subspace.domain.model.SubscriptionStatus
import app.kerrlab.subspace.domain.model.toData
import app.kerrlab.subspace.domain.model.toDomain
import arrow.core.Either
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import strikt.api.expect
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class SubscriptionServiceImplTest {

    @Test
    fun `createSubscription should return Right with SubscriptionData when save is successful`() {
        // ... (Arrange: Create test data, set up mock repository)
        // Test Data
        val subscriptionData = SubscriptionData(
            name = "Sporkify",
            billingDate = LocalDate.now(),
            startDate = LocalDate.now(),
            price = BigDecimal.valueOf(5.66),
            status = SubscriptionStatus.ACTIVE,
            endDate = null,
            id = null,
            renewalPeriod = null,
        )
        val subscription = subscriptionData.toDomain()
        val expectedResponse = SubscriptionResponseDto(1L)

        // Repositories
        val mock = mock<SubscriptionRepository> {
            on { saveSubscription(subscription.toData()) } doReturn Either.Right(expectedResponse)
        }
        val subscriptionService = SubscriptionServiceImpl(mock)
        // Test
        val result = subscriptionService.createSubscription(subscription)
        expect {
            that(result).isA<Either.Right<SubscriptionResponseDto>>().and {
                that(result.getOrNull()).isNotNull().isEqualTo(expectedResponse)
            }.not().isA<Either.Left<Any>>()
        }
    }
}