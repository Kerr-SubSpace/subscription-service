package app.kerrlab.subspace.domain.ports

import app.kerrlab.subspace.adapters.web.model.SubscriptionResponseDto
import app.kerrlab.subspace.domain.model.Subscription
import app.kerrlab.subspace.domain.model.SubscriptionError
import app.kerrlab.subspace.domain.model.SubscriptionUpdateData
import arrow.core.Either

interface SubscriptionService {

    fun createSubscription(subscription: Subscription): Either<SubscriptionError, SubscriptionResponseDto>
    fun findById(subscriptionId: Long): Either<SubscriptionError, Subscription>
    fun getAll(): Either<SubscriptionError, List<Subscription>>
    fun updateSubscription(
        id: Long,
        subscriptionUpdateData: SubscriptionUpdateData
    ): Either<SubscriptionError, Subscription>
}