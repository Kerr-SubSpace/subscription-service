package app.kerrlab.subspace.adapters.web.model

import app.kerrlab.subspace.domain.model.SubscriptionData
import app.kerrlab.subspace.domain.model.SubscriptionStatus
import app.kerrlab.subspace.domain.model.SubscriptionUpdateData
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@JsonInclude(value = NON_NULL)
data class SubscriptionWebDto(
    val id: Long? = null, // Allow null for new subscriptions
    @field:NotBlank val name: String,
    @field:Positive @field:DecimalMin("0.01") val price: BigDecimal,
    var status: SubscriptionStatus = SubscriptionStatus.ACTIVE, // Default value for new subscriptions
    @field:PastOrPresent val startDate: LocalDate, // Validation for start date
    @field:PastOrPresent val billingDate: LocalDate, // Validation for billing date, should be in the past or present
    @field:Future val endDate: LocalDate?, // Validation for end date
    val renewalPeriod: Period? = Period.ofMonths(1), // Default renewal period
)

@JsonInclude(value = NON_NULL)
data class UpdateSubscriptionDto(
    @field:PositiveOrZero val price: BigDecimal?,
    val status: SubscriptionStatus?,
    @field:Future val endDate: LocalDate?,
    @field:PastOrPresent val billingDate: LocalDate?,
    @Positive val renewalPeriod: Period?,
)

data class SubscriptionResponseDto(
    val id: Long,
    // ... other fields from SubscriptionData
)

fun UpdateSubscriptionDto.toData() = SubscriptionUpdateData(
    status = status,
    price = price,
    billingDate = billingDate,
    renewalPeriod = renewalPeriod,
    endDate = endDate,
)

fun SubscriptionWebDto.toData() = SubscriptionData(
    id = id,
    name = name,
    price = price,
    endDate = endDate,
    status = status,
    startDate = startDate,
    billingDate = billingDate,
    renewalPeriod = renewalPeriod
)