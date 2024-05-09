package app.kerrlab.subspace.domain.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

data class Subscription(
    val id: Long,
    @field:NotBlank val name: String,
    @field:Positive val price: BigDecimal,
    val status: SubscriptionStatus,
    val billingDate: LocalDate,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val renewalPeriod: Period?
)

enum class SubscriptionStatus {
    ACTIVE, PAUSED, CANCELLED
}