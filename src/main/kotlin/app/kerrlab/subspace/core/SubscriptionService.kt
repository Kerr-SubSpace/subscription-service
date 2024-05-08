package app.kerrlab.subspace.core

interface SubscriptionService {

    fun createSubscription(subscription: Subscription): Long

    fun findById(subscriptionId: Long): Subscription?

    fun updateSubscription(subscription: Subscription)

    fun getAll(): List<Subscription>
}