package app.kerrlab.subspace.controller

import app.kerrlab.subspace.dto.SubscriptionDto
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import jakarta.validation.Valid

@Controller("/subscription-service")
class SubscriptionServiceController {

    private val subscriptions = mutableListOf<SubscriptionDto>()

    @Get(uri = "/subscriptions", produces = ["application/json"])
    fun getSubscrpitions(): HttpResponse<List<SubscriptionDto>> {
        return HttpResponse.ok<List<SubscriptionDto>?>().body(subscriptions)
    }

    @Post(uri = "/subscriptions")
    fun createSubscription(@Valid @Body subscription: SubscriptionDto): HttpResponse<Long> {
        subscriptions.add(subscription)
        return HttpResponse.created(subscription.id)
    }

    @Put(uri = "/subscriptions/{id}", produces = ["application/json"])
    fun updateSubscription(@Valid @Body subscription: SubscriptionDto, @PathVariable id: Long): HttpResponse<Long> {
        TODO()
    }

    @Get(uri = "/subscriptions/{id}", produces = ["application/json"])
    fun getSubscrpitionById(id: Long): HttpResponse<SubscriptionDto> {
        val subscriptionDto = subscriptions.find { it.id == id } ?: return HttpResponse.noContent()
        return HttpResponse.ok(subscriptionDto)
    }

    @Delete(uri = "/subscriptions/{id}")
    fun deleteSubscription(id: Long): HttpResponse<String> {
        subscriptions.find { it.id == id }?.apply { subscriptions.remove(this) }
        return HttpResponse.ok()
    }
}