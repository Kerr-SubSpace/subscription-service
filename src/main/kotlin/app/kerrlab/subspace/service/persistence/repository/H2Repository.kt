package app.kerrlab.subspace.service.persistence.repository

import app.kerrlab.subspace.service.persistence.model.SubscriptionModel
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Singleton
@PersistenceContext
class H2Repository(
    private val entityManager: EntityManager
) : SubscriptionRepository {


    @ReadOnly
    override fun findById(subscriptionId: Long): SubscriptionModel? {
        return entityManager.find(SubscriptionModel::class.java, subscriptionId)
    }

    override fun find(model: SubscriptionModel): SubscriptionModel? {
        return entityManager.find(SubscriptionModel::class.java, model.id)
    }

    override fun findByName(subscriptionName: String): SubscriptionModel? {
        val query = "FROM subscription WHERE name=:subscriptionName"
        val typedQuery = entityManager.createQuery(query, SubscriptionModel::class.java)
        typedQuery.setParameter("subscriptionName", subscriptionName)
        return typedQuery.singleResult
    }

    @ReadOnly
    override fun findAll(): List<SubscriptionModel> {
        val query = "FROM subscription"
        val typedQuery = entityManager.createQuery(query, SubscriptionModel::class.java)
        return typedQuery.resultList
    }

    @Transactional
    override fun save(subscription: SubscriptionModel) {
        entityManager.persist(subscription)
    }

    @Transactional
    override fun update(subscription: SubscriptionModel): SubscriptionModel {
        return entityManager.merge(subscription)
    }

    @Transactional
    override fun delete(subscription: SubscriptionModel) {
        entityManager.remove(subscription)
    }
}