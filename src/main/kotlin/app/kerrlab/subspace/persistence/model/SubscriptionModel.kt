package app.kerrlab.subspace.persistence.model

import app.kerrlab.subspace.core.Status
import app.kerrlab.subspace.core.Subscription
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@Serdeable
@Entity(name = "subscription")
@Table(name = "subscription")
class SubscriptionModel(
    @NotNull @Column(name = "subscription_name", nullable = false) var name: String,
    @NotNull @Column(name = "status", nullable = false) var status: Status,
    @Positive @Column(name = "price", nullable = false) var price: BigDecimal,
    @Column(name = "billing_date", nullable = false) var billingDate: LocalDate,
    @Positive @Column(name = "renewal_period", nullable = false) var renewalPeriod: Period,
    @Column(name = "start_date", nullable = false) var startDate: LocalDate,
    @Column(name = "expiration_date") var expirationDate: LocalDate?,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)

fun SubscriptionModel.toEntity(): Subscription = with(this) {
    Subscription(
        name = name,
        status = status,
        price = price,
        billingDate = billingDate,
        renewalPeriod = renewalPeriod,
        startDate = startDate,
        expirationDate = expirationDate,
        id = id
    )
}

fun Subscription.toDbModel() = with(this) {
    SubscriptionModel(
        name = name,
        status = status,
        price = price,
        billingDate = billingDate,
        renewalPeriod = renewalPeriod,
        startDate = startDate,
        expirationDate = expirationDate,
        id = id
    )
}
