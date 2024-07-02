package app.kerrlab.subspace.adapters.web.model

import app.kerrlab.subspace.core.SubscriptionStatus
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@JsonInclude(value = NON_NULL)
data class CreateSubscriptionDto(
    @field:NotBlank val name: String,
    @field:Positive @field:DecimalMin("0.01") val price: BigDecimal,
    var status: SubscriptionStatus = SubscriptionStatus.ACTIVE, // Default value for new subscriptions
    @field:PastOrPresent val startDate: LocalDate, // Validation for start date
    @field:PastOrPresent val billingDate: LocalDate, // Validation for billing date, should be in the past or present
    @field:Future val endDate: LocalDate?, // Validation for end date
    val renewalPeriod: Period? = Period.ofMonths(1), // Default renewal period
)

@JsonInclude(value = NON_NULL)
data class CreateSubscriptionResponseDto(
    val id: Long
)

@JsonInclude(value = NON_NULL)
data class UpdateSubscriptionDto(
    @field:PositiveOrZero val price: BigDecimal?,
    val status: SubscriptionStatus?,
    @field:Future val endDate: LocalDate?,
    @field:PastOrPresent val billingDate: LocalDate?,
    @Positive val renewalPeriod: Period?,
)

@JsonInclude(value = NON_NULL)
data class SubscriptionDetailsDto(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val status: SubscriptionStatus,
    val startDate: String,
    val billingDate: String,
    val endDate: String?,
    val renewalPeriod: String
)