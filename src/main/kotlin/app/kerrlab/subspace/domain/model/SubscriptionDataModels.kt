package app.kerrlab.subspace.domain.model

import app.kerrlab.subspace.adapters.web.model.SubscriptionWebDto
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

data class SubscriptionData(
    val name: String,
    val price: BigDecimal,
    val status: SubscriptionStatus,
    val billingDate: LocalDate,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val renewalPeriod: Period?,
    val id: Long?
)

data class SubscriptionUpdateData(
    @field:PositiveOrZero val price: BigDecimal? = null,
    val status: SubscriptionStatus? = null,
    @field:Future val endDate: LocalDate? = null,
    @field:PastOrPresent val billingDate: LocalDate? = null,
    @Positive val renewalPeriod: Period? = null,
)

fun SubscriptionData.toDomain(id: Long = 0L): Subscription = Subscription(
    id = id, // If creating a new subscription, provide a new ID
    name = name,
    price = price,
    status = status,
    startDate = startDate,
    endDate = endDate,
    renewalPeriod = renewalPeriod,
    billingDate = billingDate
)

fun Subscription.toData(): SubscriptionData = SubscriptionData(
    id = id,
    name = name,
    price = price,
    status = status,
    startDate = startDate,
    endDate = endDate,
    renewalPeriod = renewalPeriod,
    billingDate = billingDate
)

fun Subscription.toWebDto(): SubscriptionWebDto = SubscriptionWebDto(
    id = id,
    name = name,
    price = price,
    status = status,
    startDate = startDate,
    billingDate = billingDate,
    endDate = endDate,
    renewalPeriod = renewalPeriod
)