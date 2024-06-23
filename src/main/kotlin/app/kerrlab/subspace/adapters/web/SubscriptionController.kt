package app.kerrlab.subspace.adapters.web

import app.kerrlab.subspace.adapters.web.model.SubscriptionWebDto
import app.kerrlab.subspace.adapters.web.model.UpdateSubscriptionDto
import app.kerrlab.subspace.adapters.web.model.toData
import app.kerrlab.subspace.domain.model.SubscriptionError
import app.kerrlab.subspace.domain.model.toDomain
import app.kerrlab.subspace.domain.model.toWebDto
import app.kerrlab.subspace.domain.ports.SubscriptionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@CrossOrigin(originPatterns = ["*"])
class SubscriptionController(@Autowired val subscriptionService: SubscriptionService) {
    companion object {
        const val SUBSCRIPTIONS_PATH = "/subscriptions"
    }

    @PostMapping(SUBSCRIPTIONS_PATH)
    fun createSubscription(@Valid @RequestBody subscriptionWebDto: SubscriptionWebDto): ResponseEntity<Any> {
        return subscriptionService.createSubscription(subscriptionWebDto.toData().toDomain()).fold(
            { error ->
                // Handle error cases based on the specific SubscriptionError type
                when (error) {
                    is SubscriptionError.InvalidData -> ResponseEntity.badRequest().body(error.message)
                    is SubscriptionError.NotFound -> ResponseEntity.noContent().build()
                    is SubscriptionError.AlreadyExists -> ResponseEntity.status(409).body("Subscription already exists")
                }
            },
            { response ->
                // Handle success
                ResponseEntity.created(URI("/subscriptions/${response.id}"))
                    .body(response) // Return created response with data and URI

            })
    }

    @GetMapping(SUBSCRIPTIONS_PATH)
    fun getSubscriptions(): ResponseEntity<Any> {
        return subscriptionService.getAll().fold(
            { error ->
                when (error) {
                    is SubscriptionError.NotFound -> ResponseEntity.notFound().build()
                    else -> ResponseEntity.internalServerError().build()
                }
            },
            { data ->
                ResponseEntity.ok(data.map { it.toWebDto() })
            }
        )
    }

    @GetMapping("$SUBSCRIPTIONS_PATH/{id}")
    fun getSubscriptionById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return subscriptionService.findById(id).fold(
            { error ->
                when (error) {
                    is SubscriptionError.NotFound -> ResponseEntity.noContent().build()
                    else -> ResponseEntity.internalServerError().build()
                }
            },
            { data ->
                ResponseEntity.ok(data.toWebDto())
            }
        )
    }

    @PutMapping("$SUBSCRIPTIONS_PATH/{id}")
    fun updateSubscription(
        @RequestBody updateSubscriptionDto: UpdateSubscriptionDto,
        @PathVariable("id") id: Long
    ): ResponseEntity<Any> {
        return subscriptionService.updateSubscription(
            id,
            updateSubscriptionDto.toData()
        ).fold(
            { error ->
                when (error) {
                    is SubscriptionError.NotFound -> ResponseEntity.noContent().build()
                    is SubscriptionError.InvalidData -> ResponseEntity.badRequest().body(error.message)
                    else -> ResponseEntity.internalServerError().build()
                }
            },
            { data ->
                ResponseEntity.ok(data.toWebDto())
            }
        )
    }

    @DeleteMapping("$SUBSCRIPTIONS_PATH/{id}")
    fun deleteSubscription(
        @PathVariable("id") id: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }
}