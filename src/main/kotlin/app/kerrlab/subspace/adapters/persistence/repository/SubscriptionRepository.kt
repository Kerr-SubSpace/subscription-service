package app.kerrlab.subspace.adapters.persistence.repository

import app.kerrlab.subspace.adapters.web.model.SubscriptionResponseDto
import app.kerrlab.subspace.domain.model.SubscriptionData
import app.kerrlab.subspace.domain.model.SubscriptionError
import app.kerrlab.subspace.domain.model.SubscriptionUpdateData
import arrow.core.Either

interface SubscriptionRepository {

    fun saveSubscription(subscriptionData: SubscriptionData): Either<SubscriptionError, SubscriptionResponseDto>
    fun findAll(): Either<SubscriptionError, List<SubscriptionData>>
    fun findById(id: Long): Either<SubscriptionError, SubscriptionData>
    fun updateSubscription(
        id: Long,
        subscriptionUpdateData: SubscriptionUpdateData
    ): Either<SubscriptionError, SubscriptionData>

}