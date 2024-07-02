package app.kerrlab.subspace.adapters.persistence

import app.kerrlab.subspace.adapters.persistence.model.SubscriptionEntity
import app.kerrlab.subspace.adapters.persistence.model.UpdateEntityDto
import app.kerrlab.subspace.core.SubscriptionError
import app.kerrlab.subspace.ports.persistence.SubscriptionRepository
import arrow.core.Either
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
class SubscriptionHibernateAdapter(
    @Autowired private val subscriptionJpaRepository: SubscriptionJpaRepository
) : SubscriptionRepository {

    @Query
    override fun saveSubscription(subscriptionEntity: SubscriptionEntity): Either<SubscriptionError, SubscriptionEntity> {
        return try {
            val savedEntity = subscriptionJpaRepository.save(subscriptionEntity)
            Either.Right(savedEntity)
        } catch (e: ConstraintViolationException) {
            Either.Left(SubscriptionError.AlreadyExists)
        } catch (e: Exception) {
            Either.Left(SubscriptionError.InvalidData(e.message ?: "Bad data when saving to DB"))
        }
    }

    override fun findById(id: Long): Either<SubscriptionError, SubscriptionEntity> {
        return try {
            val maybeSubscriptionEntity = subscriptionJpaRepository.findById(id)
            if (maybeSubscriptionEntity.isPresent) {
                val subscriptionData = maybeSubscriptionEntity.map { it }.get()
                Either.Right(subscriptionData)
            } else {
                Either.Left(SubscriptionError.NotFound)
            }
        } catch (e: Exception) {
            Either.Left(SubscriptionError.InvalidData(e.message ?: "Unknown error"))
        }
    }

    override fun findAll(): Either<SubscriptionError, List<SubscriptionEntity>> {
        return try {
            val all = subscriptionJpaRepository.findAll().map { it }
            Either.Right(all)
        } catch (e: Exception) {
            Either.Left(SubscriptionError.NotFound)
        }
    }

    override fun updateSubscription(updateSubscriptionDto: UpdateEntityDto, id: Long): Either<SubscriptionError, Unit> {
        return try {
            val result = subscriptionJpaRepository.updateSubscription(updateSubscriptionDto, id)
            Either.Right(result)
        } catch (e: Exception) {
            //TODO: don't leak SQL here
            Either.Left(SubscriptionError.UnknownError(e.message ?: "Unknown error"))
        }
    }

    override fun delete(id: Long): Either<SubscriptionError, Unit> {
        return try {
            val result = subscriptionJpaRepository.deleteById(id)
            Either.Right(result)
        } catch (e: Exception) {
            Either.Left(SubscriptionError.UnknownError(e.message ?: "Unknown error"))
        }
    }
}