package app.kerrlab.subspace.adapters.persistence.model

import app.kerrlab.subspace.adapters.web.model.SubscriptionResponseDto
import app.kerrlab.subspace.domain.model.SubscriptionData
import app.kerrlab.subspace.domain.model.SubscriptionStatus
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
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

    companion object {
        @JvmStatic
        fun from(data: SubscriptionData) = SubscriptionEntity(
            name = data.name,
            status = data.status,
            price = data.price,
            billingDate = data.billingDate,
            renewalPeriod = data.renewalPeriod,
            startDate = data.startDate,
            endDate = data.endDate,
        )
    }
}

fun SubscriptionEntity.toData() = SubscriptionData(
    id = this.id,
    name = this.name,
    status = this.status,
    price = this.price,
    billingDate = this.billingDate,
    renewalPeriod = this.renewalPeriod,
    startDate = this.startDate,
    endDate = this.endDate,
)

fun SubscriptionEntity.toSubscriptionResponseDto(): SubscriptionResponseDto {
    val idValue = this.id ?: throw IllegalStateException("Subscription ID cannot be null for DTO conversion")
    return SubscriptionResponseDto(idValue)
}
