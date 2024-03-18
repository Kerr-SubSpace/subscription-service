package app.kerrlab.subspace

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("/subscription-service")
class SubscriptionServiceController {

    @Get(uri = "/", produces = ["text/plain"])
    fun index(): String {
        return "Example Response"
    }

    @Get(uri = "/subscriptions", produces = ["text/plain"])
    fun getSubscrpitions(): String {
        return "all subscriptions"
    }

    @Post(uri = "/subscriptions")
    fun createSubscription(subscriptionDto: Any): HttpResponse<String> {
        return HttpResponse.created("Done!")
    }

    @Get(uri = "/subscriptions/{id}", produces = ["text/plain"])
    fun getSubscrpitionById(id: Long): String {
        return "subscription with id $id"
    }

    @Delete(uri = "/subscriptions/{id}")
    fun deleteSubscription(id: Long): HttpResponse<String> {
        return HttpResponse.ok("Deleted")
    }
}