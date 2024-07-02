package app.kerrlab.subspace.ports.web

import app.kerrlab.subspace.adapters.web.model.CreateSubscriptionDto
import app.kerrlab.subspace.adapters.web.model.CreateSubscriptionResponseDto
import app.kerrlab.subspace.adapters.web.model.SubscriptionDetailsDto
import app.kerrlab.subspace.adapters.web.model.UpdateSubscriptionDto
import app.kerrlab.subspace.core.SubscriptionError
import arrow.core.Either

interface SubscriptionService {

    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Either<SubscriptionError, CreateSubscriptionResponseDto>
    fun findById(subscriptionId: Long): Either<SubscriptionError, SubscriptionDetailsDto>
    fun getAll(): Either<SubscriptionError, List<SubscriptionDetailsDto>>
    fun updateSubscription(
        id: Long,
        updateSubscriptionDto: UpdateSubscriptionDto,
    ): Either<SubscriptionError, Unit>

    fun deleteSubscription(id: Long): Either<SubscriptionError, Unit>
}