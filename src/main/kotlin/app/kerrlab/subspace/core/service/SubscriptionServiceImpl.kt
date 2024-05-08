package app.kerrlab.subspace.core.service

import app.kerrlab.subspace.core.Subscription
import app.kerrlab.subspace.core.SubscriptionService
import app.kerrlab.subspace.persistence.model.toDbModel
import app.kerrlab.subspace.persistence.model.toEntity
import app.kerrlab.subspace.core.repository.SubscriptionRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SubscriptionServiceImpl(
    @Inject private val subscriptionRepository: SubscriptionRepository
) : SubscriptionService {
    override fun createSubscription(subscription: Subscription): Long {
        val dbSubscription = subscription.toDbModel()
        subscriptionRepository.save(dbSubscription)
        return dbSubscription.id!!
    }

    override fun findById(subscriptionId: Long): Subscription? {
        return subscriptionRepository.findById(subscriptionId)?.toEntity()
    }

    override fun updateSubscription(subscription: Subscription) {
        subscriptionRepository.update(subscription.toDbModel())
    }

    override fun getAll(): List<Subscription> {
        return subscriptionRepository.findAll().map { it.toEntity() }
    }
}