package app.kerrlab.subspace.adapters.persistence.model

import app.kerrlab.subspace.core.SubscriptionStatus
import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period


@Table(name = "subscription")
@Entity(name = "subscription")
class SubscriptionEntity(
    @NotNull @Column(name = "subscription_name", nullable = false) var name: String,
    @NotNull @Column(name = "status", nullable = false) var status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    @Positive @Column(name = "price", nullable = false) var price: BigDecimal,
    @Column(name = "billing_date", nullable = false) var billingDate: LocalDate,
    @Positive @Column(name = "renewal_period", nullable = false) var renewalPeriod: Period?,
    @Column(name = "start_date", nullable = false) var startDate: LocalDate,
    @Column(name = "expiration_date") var endDate: LocalDate?,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
) {
    init {
        this.endDate = this.startDate.plus(renewalPeriod)
    }
}

data class UpdateEntityDto(
    @field:PositiveOrZero val price: BigDecimal?,
    val status: SubscriptionStatus?,
    @field:Future val endDate: LocalDate?,
    @field:PastOrPresent val billingDate: LocalDate?,
    @Positive val renewalPeriod: Period?,
)