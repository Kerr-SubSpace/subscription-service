package app.kerrlab.subspace.api.model

import app.kerrlab.subspace.core.Status
import app.kerrlab.subspace.core.Subscription
import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable.Deserializable
import io.micronaut.serde.annotation.Serdeable.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@Serializable
@Deserializable
@JsonInclude(JsonInclude.Include.NON_NULL)
class SubscriptionDTO(
    val name: String,
    val price: BigDecimal,
    val billingDate: LocalDate,
    val renewalPeriod: Period = Period.ofMonths(1),
    val startDate: LocalDate = LocalDate.now(),
    val status: Status = Status.ACTIVE,
    var expirationDate: LocalDate? = null,
    val id: Long? = null
)

fun SubscriptionDTO.toEntity() = with(this) {
    Subscription(
        name = name,
        price = price,
        billingDate = billingDate,
        renewalPeriod = renewalPeriod,
        startDate = startDate,
        status = status,
        expirationDate = expirationDate,
        id = id
    )
}

fun Subscription.toDTO() = with(this) {
    SubscriptionDTO(
        name = name,
        price = price,
        billingDate = billingDate,
        renewalPeriod = renewalPeriod,
        startDate = startDate,
        status = status,
        expirationDate = expirationDate,
        id = id
    )
}
