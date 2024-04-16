package app.kerrlab.subspace.controller

import app.kerrlab.subspace.dto.Subscription
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/subscription-service")
class SubscriptionServiceController {

    private val subscriptions = mutableListOf<Subscription>()

    @Get(uri = "/subscriptions", produces = ["application/json"])
    fun getSubscriptions(): HttpResponse<List<Subscription>> {
        return HttpResponse.ok<List<Subscription>?>().body(subscriptions)
    }

    @Post(uri = "/subscriptions")
    fun createSubscription(@Valid @Body subscription: Subscription): HttpResponse<Long> {
        subscriptions.add(subscription)
        return HttpResponse.created(subscription.id)
    }

    @Put(uri = "/subscriptions/{id}", produces = ["application/json"])
    fun updateSubscription(@Valid @Body subscription: Subscription, @PathVariable id: Long): HttpResponse<Long> {
        TODO()
    }

    @Get(uri = "/subscriptions/{id}", produces = ["application/json"])
    fun getSubscriptionById(id: Long): HttpResponse<Subscription> {
        val subscription = subscriptions.find { it.id == id } ?: return HttpResponse.noContent()
        return HttpResponse.ok(subscription)
    }

    @Delete(uri = "/subscriptions/{id}")
    fun deleteSubscription(id: Long): HttpResponse<String> {
        subscriptions.find { it.id == id }?.apply { subscriptions.remove(this) }
        return HttpResponse.ok()
    }
}