package app.kerrlab.subspace.core

import app.kerrlab.subspace.adapters.persistence.model.UpdateEntityDto
import app.kerrlab.subspace.adapters.web.model.CreateSubscriptionDto
import app.kerrlab.subspace.adapters.web.model.CreateSubscriptionResponseDto
import app.kerrlab.subspace.adapters.web.model.SubscriptionDetailsDto
import app.kerrlab.subspace.adapters.web.model.UpdateSubscriptionDto
import app.kerrlab.subspace.ports.persistence.SubscriptionRepository
import app.kerrlab.subspace.ports.web.SubscriptionService
import arrow.core.Either
import org.springframework.stereotype.Service

@Service
class SubscriptionServiceImpl(
    private val subscriptionRepository: SubscriptionRepository
) : SubscriptionService {

    override fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Either<SubscriptionError, CreateSubscriptionResponseDto> {
        val subscription = Subscription(
            name = createSubscriptionDto.name,
            price = createSubscriptionDto.price,
            status = createSubscriptionDto.status,
            billingDate = createSubscriptionDto.billingDate,
            startDate = createSubscriptionDto.startDate,
            endDate = createSubscriptionDto.endDate,
            renewalPeriod = createSubscriptionDto.renewalPeriod,
            id = null
        )
        return subscriptionRepository.saveSubscription(subscription.toEntity())
            // TODO: find a better way to decorate entity with ID than "id!!"
            .map { CreateSubscriptionResponseDto(it.id!!) }
    }

    override fun findById(subscriptionId: Long): Either<SubscriptionError, SubscriptionDetailsDto> {
        return subscriptionRepository.findById(subscriptionId).map { it.toSubscription().toSubscriptionDetails() }
    }

    override fun getAll(): Either<SubscriptionError, List<SubscriptionDetailsDto>> {
        return subscriptionRepository.findAll().fold({ error ->
            return Either.Left(error)
        }, { data ->
            data.map { it.toSubscription().toSubscriptionDetails() }.let { Either.Right(it) }

        })
    }

    override fun updateSubscription(
        id: Long, updateSubscriptionDto: UpdateSubscriptionDto
    ): Either<SubscriptionError, Unit> {
        val updatedDto = UpdateEntityDto(
            status = updateSubscriptionDto.status,
            price = updateSubscriptionDto.price,
            renewalPeriod = updateSubscriptionDto.renewalPeriod,
            endDate = updateSubscriptionDto.endDate,
            billingDate = updateSubscriptionDto.billingDate,
        )
        return subscriptionRepository.updateSubscription(updatedDto, id)
    }

    override fun deleteSubscription(id: Long): Either<SubscriptionError, Unit> {
        return subscriptionRepository.delete(id)
    }
}