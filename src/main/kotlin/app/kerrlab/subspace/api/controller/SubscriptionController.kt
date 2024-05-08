package app.kerrlab.subspace.api.controller

import app.kerrlab.subspace.api.model.SubscriptionDTO
import app.kerrlab.subspace.api.model.toDTO
import app.kerrlab.subspace.api.model.toEntity
import app.kerrlab.subspace.core.SubscriptionService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.inject.Inject
import jakarta.validation.Valid

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/subscription-service")
class SubscriptionController(@Inject val subscriptionService: SubscriptionService) {
    companion object {
        const val SUBSCRIPTIONS_PATH = "/subscriptions"
    }

    @Post(uri = SUBSCRIPTIONS_PATH)
    fun createSubscription(@Valid @Body subscriptionDTO: SubscriptionDTO): HttpResponse<Long> {
        val id = subscriptionService.createSubscription(subscriptionDTO.toEntity())
        return HttpResponse.created(id)
    }

    @Get(uri = SUBSCRIPTIONS_PATH, produces = ["application/json"])
    fun getSubscriptions(): HttpResponse<List<SubscriptionDTO>> {
        val all = subscriptionService.getAll().map { it.toDTO() }
        return HttpResponse.ok<List<SubscriptionDTO>>().body(all)
    }

    @Get(uri = "$SUBSCRIPTIONS_PATH/{id}", produces = ["application/json"])
    fun getSubscriptionById(id: Long): HttpResponse<SubscriptionDTO> {
        val retrievedSubscription = subscriptionService.findById(id)
            ?: return HttpResponse.notFound()
        return retrievedSubscription.toDTO().let { HttpResponse.ok(it) }
    }

    @Put(uri = "$SUBSCRIPTIONS_PATH/{id}", produces = ["application/json"])
    fun updateSubscription(
        @Valid @Body subscriptionDTO: SubscriptionDTO,
        @PathVariable id: Long
    ): HttpResponse<SubscriptionDTO> {
        subscriptionService.updateSubscription(subscriptionDTO.toEntity())
        return HttpResponse.ok(subscriptionDTO)
    }
}