package app.kerrlab.subspace.service.persistence.repository

import app.kerrlab.subspace.service.persistence.model.SubscriptionModel

interface SubscriptionRepository {

    fun findById(subscriptionId: Long): SubscriptionModel?

    fun find(model: SubscriptionModel): SubscriptionModel?

    fun findByName(subscriptionName: String): SubscriptionModel?

    fun findAll(): List<SubscriptionModel>

    fun save(subscription: SubscriptionModel)

    fun update(subscription: SubscriptionModel): SubscriptionModel

    fun delete(subscription: SubscriptionModel)
}
