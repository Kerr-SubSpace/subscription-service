package app.kerrlab.subspace.core

import app.kerrlab.subspace.adapters.persistence.model.SubscriptionEntity
import app.kerrlab.subspace.adapters.web.model.SubscriptionDetailsDto
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class Subscription(
    val id: Long?, // Allow null for new subscriptions
    @field:NotBlank val name: String,
    @field:Positive @field:DecimalMin("0.01") val price: BigDecimal,
    val status: SubscriptionStatus,
    val billingDate: LocalDate,
    @field:PastOrPresent val startDate: LocalDate,
    @field:Future val endDate: LocalDate?,
    val renewalPeriod: Period?
)

enum class SubscriptionStatus {
    ACTIVE, PAUSED, CANCELLED
}

fun Subscription.toEntity() = SubscriptionEntity(
    id = this.id,
    name = this.name,
    price = this.price,
    status = this.status,
    billingDate = this.billingDate,
    startDate = this.startDate,
    endDate = this.endDate,
    renewalPeriod = this.renewalPeriod
)

fun SubscriptionEntity.toSubscription() = Subscription(
    id = this.id,
    name = this.name,
    price = this.price,
    status = this.status,
    billingDate = this.billingDate,
    startDate = this.startDate,
    endDate = this.endDate,
    renewalPeriod = this.renewalPeriod
)

fun Subscription.toSubscriptionDetails() = SubscriptionDetailsDto(
    id = this.id!!,
    name = this.name,
    price = this.price,
    status = this.status,
    billingDate = this.billingDate.format(DateTimeFormatter.ISO_DATE),
    startDate = this.startDate.format(DateTimeFormatter.ISO_DATE),
    endDate = this.endDate?.format(DateTimeFormatter.ISO_DATE),
    renewalPeriod = this.renewalPeriod.toString()
)