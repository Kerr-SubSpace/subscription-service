package app.kerrlab.subspace.dto

import io.micronaut.serde.annotation.Serdeable.Deserializable
import io.micronaut.serde.annotation.Serdeable.Serializable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import kotlin.random.Random

@Serializable
@Deserializable
data class Subscription(
    @field:NotNull val id: Long = Random.nextLong(0, Long.MAX_VALUE),
    @field:NotBlank val name: String,
    @field:Positive val price: BigDecimal,
    @field:NotNull val startDate: LocalDate = LocalDate.now(),
    var status: Status? = null,
    var endDate: LocalDate? = null,
    val renewalPeriod: Period? = Period.ofMonths(1),
) {
    enum class Status {
        ACTIVE, PAUSED, CANCELLED
    }
}