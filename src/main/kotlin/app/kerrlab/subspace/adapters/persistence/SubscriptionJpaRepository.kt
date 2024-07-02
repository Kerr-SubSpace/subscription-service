package app.kerrlab.subspace.adapters.persistence

import app.kerrlab.subspace.adapters.persistence.model.SubscriptionEntity
import app.kerrlab.subspace.adapters.persistence.model.UpdateEntityDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SubscriptionJpaRepository : JpaRepository<SubscriptionEntity, Long> {

    @Transactional
    @Modifying
    @Query(
        "UPDATE subscription s SET " +
                "s.price = COALESCE(:#{#dto.price}, s.price), " +
                "s.status = COALESCE(:#{#dto.status}, s.status), " +
                "s.endDate = COALESCE(:#{#dto.endDate}, s.endDate), " +
                "s.billingDate = COALESCE(:#{#dto.billingDate}, s.billingDate), " +
                "s.renewalPeriod = COALESCE(:#{#dto.renewalPeriod}, s.renewalPeriod) " +
                "WHERE s.id = :id"
    )
    fun updateSubscription(@Param("dto") data: UpdateEntityDto, @Param("id") id: Long)

}