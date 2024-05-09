package app.kerrlab.subspace.adapters.persistence.repository.spring

import app.kerrlab.subspace.adapters.persistence.model.SubscriptionEntity
import app.kerrlab.subspace.domain.model.SubscriptionUpdateData
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
                "s.price = :#{#dto.price}, " +
                "s.status = :#{#dto.status}, " +
                "s.endDate = :#{#dto.endDate}, " +
                "s.billingDate = :#{#dto.billingDate}, " +
                "s.renewalPeriod = :#{#dto.renewalPeriod} " +
                "WHERE s.id = :id"
    )
    fun updateSubscription(@Param("dto") data: SubscriptionUpdateData, @Param("id") id: Long)

}