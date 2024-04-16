package app.kerrlab.subspace.service.persistence.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Serdeable
@Entity(name = "subscription")
@Table(name = "subscription")
class SubscriptionModel(
    @NotNull @Column(name = "subscription_name", nullable = false) var name: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
){
    override fun toString() = "SubscriptionModel(name='$name', id=$id)"
}