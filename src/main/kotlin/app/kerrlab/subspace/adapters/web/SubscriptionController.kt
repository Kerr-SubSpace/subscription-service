package app.kerrlab.subspace.adapters.web

import app.kerrlab.subspace.adapters.web.model.CreateSubscriptionDto
import app.kerrlab.subspace.adapters.web.model.UpdateSubscriptionDto
import app.kerrlab.subspace.core.SubscriptionError
import app.kerrlab.subspace.ports.web.SubscriptionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
class SubscriptionController(@Autowired val subscriptionService: SubscriptionService) {
    companion object {
        const val SUBSCRIPTIONS_PATH = "/subscriptions"
    }

    @PostMapping(SUBSCRIPTIONS_PATH)
    fun createSubscription(@Valid @RequestBody createSubscriptionDto: CreateSubscriptionDto): ResponseEntity<Any> {
        return subscriptionService.createSubscription(createSubscriptionDto).fold({ error ->
            // Handle error cases based on the specific SubscriptionError type
            when (error) {
                is SubscriptionError.InvalidData -> ResponseEntity.badRequest().body(error.message)
                is SubscriptionError.NotFound -> ResponseEntity.noContent().build()
                is SubscriptionError.AlreadyExists -> ResponseEntity.status(409).body("Subscription already exists")
                is SubscriptionError.UnknownError -> ResponseEntity.internalServerError().body("Unknown error")
            }
        }, { response ->
            // Handle success
            ResponseEntity.created(URI("/subscriptions/${response.id}"))
                .body(response) // Return created response with data and URI

        })
    }

    @GetMapping(SUBSCRIPTIONS_PATH)
    fun getSubscriptions(): ResponseEntity<Any> {
        return subscriptionService.getAll().fold({ error ->
            when (error) {
                is SubscriptionError.NotFound -> ResponseEntity.notFound().build()
                else -> ResponseEntity.internalServerError().build()
            }
        }, { data ->
            ResponseEntity.ok(data)
        })
    }

    @GetMapping("$SUBSCRIPTIONS_PATH/{id}")
    fun getSubscriptionById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return subscriptionService.findById(id).fold({ error ->
            when (error) {
                is SubscriptionError.NotFound -> ResponseEntity.noContent().build()
                else -> ResponseEntity.internalServerError().build()
            }
        }, { data ->
            ResponseEntity.ok(data)
        })
    }

    @PutMapping("$SUBSCRIPTIONS_PATH/{id}")
    fun updateSubscription(
        @RequestBody updateSubscriptionDto: UpdateSubscriptionDto, @PathVariable("id") id: Long
    ): ResponseEntity<Any> {
        return subscriptionService.updateSubscription(
            id, updateSubscriptionDto
        ).fold({ error ->
            when (error) {
                is SubscriptionError.NotFound -> ResponseEntity.noContent().build()
                is SubscriptionError.InvalidData -> ResponseEntity.badRequest().body(error.message)
                else -> ResponseEntity.internalServerError().build()
            }
        }, { _ -> ResponseEntity.ok().build() })
    }

    @DeleteMapping("$SUBSCRIPTIONS_PATH/{id}")
    fun deleteSubscription(
        @PathVariable("id") id: Long
    ): ResponseEntity<Any> {
        return subscriptionService.deleteSubscription(id)
            .fold({ error -> ResponseEntity.internalServerError().body(error) }, { _ -> ResponseEntity.ok().build() })
    }
}