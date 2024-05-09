package app.kerrlab.subspace.adapters.persistence.repository.spring

import app.kerrlab.subspace.adapters.persistence.model.SubscriptionEntity
import app.kerrlab.subspace.adapters.persistence.model.toData
import app.kerrlab.subspace.adapters.persistence.model.toSubscriptionResponseDto
import app.kerrlab.subspace.adapters.persistence.repository.SubscriptionRepository
import app.kerrlab.subspace.adapters.web.model.SubscriptionResponseDto
import app.kerrlab.subspace.domain.model.SubscriptionData
import app.kerrlab.subspace.domain.model.SubscriptionError
import app.kerrlab.subspace.domain.model.SubscriptionUpdateData
import arrow.core.Either
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
class SpringJpaSubscriptionRepository(
    @Autowired private val subscriptionJpaRepository: SubscriptionJpaRepository
) : SubscriptionRepository {

    @Query
    override fun saveSubscription(subscriptionData: SubscriptionData): Either<SubscriptionError, SubscriptionResponseDto> {
        return try {
            val subscriptionEntity = SubscriptionEntity.from(subscriptionData)
            val savedEntity = subscriptionJpaRepository.save(subscriptionEntity)
            Either.Right(savedEntity.toSubscriptionResponseDto())
        } catch (e: ConstraintViolationException) {
            Either.Left(SubscriptionError.AlreadyExists)
        } catch (e: Exception) {
            Either.Left(SubscriptionError.InvalidData(e.message ?: "Bad data when saving to DB"))
        }
    }

    override fun findById(id: Long): Either<SubscriptionError, SubscriptionData> {
        return try {
            val maybeSubscriptionEntity = subscriptionJpaRepository.findById(id)
            if (maybeSubscriptionEntity.isPresent) {
                val subscriptionData = maybeSubscriptionEntity.map { it.toData() }.get()
                Either.Right(subscriptionData)
            } else {
                Either.Left(SubscriptionError.NotFound)
            }
        } catch (e: Exception) {
            Either.Left(SubscriptionError.InvalidData(e.message ?: "Unknown error"))
        }
    }

    override fun findAll(): Either<SubscriptionError, List<SubscriptionData>> {
        return try {
            val all = subscriptionJpaRepository.findAll().map { it.toData() }
            Either.Right(all)
        } catch (e: Exception) {
            Either.Left(SubscriptionError.NotFound)
        }
    }

    override fun updateSubscription(
        id: Long,
        subscriptionUpdateData: SubscriptionUpdateData
    ): Either<SubscriptionError, SubscriptionData> {
        return try {
            subscriptionJpaRepository.updateSubscription(subscriptionUpdateData, id)
            findById(id)
        } catch (e: Exception) {
            //TODO: don't leak SQL here
            Either.Left(SubscriptionError.InvalidData(e.message ?: "Unknown error"))
        }
    }
}