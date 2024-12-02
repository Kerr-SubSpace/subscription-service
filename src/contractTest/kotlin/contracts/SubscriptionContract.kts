package contracts

import org.springframework.cloud.contract.spec.ContractDsl.Companion.contract

contract {
    description = "Should return all subscriptions with pagination"
    request {
        url = url("/subscriptions")
        method = GET
        headers {
            contentType = "application/json"
        }
        body {
            mapOf(
                "id" to v(anyNumber)
            )
        }
    }
    response {
        status = OK
    }
}

contract {
    description = "Should create a new subscription"
    request {
        url = url("/subscriptions")
        method = POST
        headers {
            contentType = "application/json"
        }
        body(
            mapOf(
                "name" to v("name"),
                "price" to v("price"),
                "status" to v("status"),
                "startDate" to v("startDate"),
                "endDate" to v("endDate"),
                "billingDate" to v("billingDate"),
                "renewalPeriod" to v("renewalPeriod"),
            )
        )
    }
    response {
        status = CREATED
        headers {
            contentType = "application/json"
            location = "/subscriptions/3"
        }
        body(
            mapOf(
                "id" to 3,
                "name" to "New subscription",
                "price" to 19.99,
                "status" to "ACTIVE",
                "startDate" to v("startDate"),
                "endDate" to v("endDate"),
                "billingDate" to v("billingDate"),
                "renewalPeriod" to v("renewalPeriod"),
            )
        )
    }
}