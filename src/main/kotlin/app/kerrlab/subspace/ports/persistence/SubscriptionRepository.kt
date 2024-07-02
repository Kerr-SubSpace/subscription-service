package app.kerrlab.subspace.ports.persistence

import app.kerrlab.subspace.adapters.persistence.model.SubscriptionEntity
import app.kerrlab.subspace.adapters.persistence.model.UpdateEntityDto
import app.kerrlab.subspace.core.SubscriptionError
import arrow.core.Either

interface SubscriptionRepository {

    fun saveSubscription(subscriptionEntity: SubscriptionEntity): Either<SubscriptionError, SubscriptionEntity>
    fun findAll(): Either<SubscriptionError, List<SubscriptionEntity>>
    fun findById(id: Long): Either<SubscriptionError, SubscriptionEntity>
    fun updateSubscription(updateSubscriptionDto: UpdateEntityDto, id: Long): Either<SubscriptionError, Unit>
    fun delete(id: Long): Either<SubscriptionError, Unit>

}