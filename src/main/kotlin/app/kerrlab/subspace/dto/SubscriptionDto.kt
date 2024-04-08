package app.kerrlab.subspace.dto

import io.micronaut.serde.annotation.Serdeable.Deserializable
import io.micronaut.serde.annotation.Serdeable.Serializable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import kotlin.random.Random

@Serializable
@Deserializable
data class SubscriptionDto(
    @field:NotBlank
    val name: String,
    @field:Positive
    val price: BigDecimal,
    val startDate: LocalDate = LocalDate.now(),
    val renewalPeriod: Period? = Period.ofMonths(1)
) {
    val id: Long = Random.nextLong(0, Long.MAX_VALUE)
    var status: Status? = null
    var endDate: LocalDate? = null
}

enum class Status {
    ACTIVE,
    PAUSED,
    CANCELLED
}