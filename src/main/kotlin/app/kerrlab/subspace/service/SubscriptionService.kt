package app.kerrlab.subspace.service

import app.kerrlab.subspace.dto.Subscription

interface SubscriptionService {

    fun createSubscription(subscription: Subscription): Int

    fun cancelSubscription(subscription: Subscription)

    fun pauseSubscription(subscription: Subscription)

    fun resumeSubscription(subscription: Subscription)

}