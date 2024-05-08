package app.kerrlab.subspace.core

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

data class Subscription(
    @field:NotBlank val name: String,
    @field:Positive val price: BigDecimal,
    @field:NotNull val billingDate: LocalDate,
    val renewalPeriod: Period = Period.ofMonths(1),
    val startDate: LocalDate = LocalDate.now(),
    val status: Status = Status.ACTIVE,
    var expirationDate: LocalDate? = null,
    val id: Long? = null
)

enum class Status {
    ACTIVE, PAUSED, CANCELLED
}