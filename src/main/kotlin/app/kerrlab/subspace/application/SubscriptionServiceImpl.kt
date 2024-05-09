package app.kerrlab.subspace.application

import app.kerrlab.subspace.adapters.persistence.repository.SubscriptionRepository
import app.kerrlab.subspace.adapters.web.model.SubscriptionResponseDto
import app.kerrlab.subspace.domain.model.*
import app.kerrlab.subspace.domain.ports.SubscriptionService
import arrow.core.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SubscriptionServiceImpl(
    @Autowired private val subscriptionRepository: SubscriptionRepository
) : SubscriptionService {

    override fun createSubscription(subscription: Subscription): Either<SubscriptionError, SubscriptionResponseDto> {
        return subscriptionRepository.saveSubscription(subscription.toData())
    }

    override fun findById(subscriptionId: Long): Either<SubscriptionError, Subscription> {
        // TODO: find a better way to decorate entity with ID than "id!!"
        return subscriptionRepository.findById(subscriptionId).map { it.toDomain(it.id!!) }
    }

    override fun updateSubscription(
        id: Long,
        subscriptionUpdateData: SubscriptionUpdateData
    ): Either<SubscriptionError, Subscription> {
        return subscriptionRepository.updateSubscription(
            id = id,
            subscriptionUpdateData = subscriptionUpdateData
        )
            .map { it.toDomain(it.id!!) }
    }

    override fun getAll(): Either<SubscriptionError, List<Subscription>> {
        return subscriptionRepository.findAll().map { list -> list.map { it.toDomain(it.id!!) } }
    }
}