package app.kerrlab.subspace

import app.kerrlab.subspace.core.Status
import app.kerrlab.subspace.core.Subscription
import app.kerrlab.subspace.persistence.model.SubscriptionModel
import app.kerrlab.subspace.persistence.model.toDbModel
import app.kerrlab.subspace.core.repository.SubscriptionRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.EntityExistsException
import jakarta.persistence.EntityManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@MicronautTest
class PersistenceTest(
    @Inject private val subscriptionRepository: SubscriptionRepository,
    @Inject private val entityManager: EntityManager
) : FeatureSpec(
    {
        val logger: Logger = LoggerFactory.getLogger(PersistenceTest::class.java)

        fun generateSubscription(name: String) = Subscription(
            name = name,
            price = BigDecimal.valueOf(3.50),
            billingDate = LocalDate.now()
        ).toDbModel()

        feature("persistence context") {
            scenario("should not be null") {
                entityManager.shouldNotBeNull()
                subscriptionRepository.shouldNotBeNull()
            }


            scenario("should allow saving new entities") {
                val subscription = generateSubscription("Spongify")
                logger.info("Attempting to save subscription {}", subscription)
                subscriptionRepository.save(subscription)
                entityManager.contains(subscription).shouldBeTrue()
                logger.info("Successfully saved subscription {}", subscription)
            }

            scenario("should find entity by id") {
                val subscription = generateSubscription("Wamazon")
                subscriptionRepository.save(subscription)
                subscription.id?.shouldNotBeNull()
                logger.info("Retrieving subscription with id {}", subscription.id)
                val savedSubscription = subscriptionRepository.findById(subscription.id!!)
                savedSubscription.shouldNotBeNull()
                savedSubscription.name shouldBe "Wamazon"
            }

            scenario("should find all entities") {
                val subscription = generateSubscription("Nerdflix")
                subscriptionRepository.save(subscription)
                subscription.id?.shouldNotBeNull()
                logger.info("Retrieving subscription with id {}", subscription.id)
                val allSubscriptions = subscriptionRepository.findAll()
                logger.info("Retrieved list of subscriptions {}", allSubscriptions)
                allSubscriptions.shouldNotBeEmpty()
                allSubscriptions.shouldContain(subscription)

            }

            scenario("should find by name") {
                val subscription = generateSubscription("CornPub")
                subscriptionRepository.save(subscription)
                logger.info("Saved subscription {}", subscription)
                val savedSubscription = subscriptionRepository.findByName("CornPub")
                savedSubscription.shouldNotBeNull()
                savedSubscription shouldBeEqual subscription
            }

            scenario("should allow updating entities") {
                val subscription = generateSubscription("WeTube Premium")
                subscriptionRepository.save(subscription)
                val subInDb = subscriptionRepository.findById(subscription.id!!)
                subInDb.shouldNotBeNull()
                subInDb.name shouldBe "WeTube Premium"
                subscription.name = "WeTube Premium+"
                subscriptionRepository.update(subscription)
                subscriptionRepository.findById(subscription.id!!)?.name shouldBe "WeTube Premium+"
            }

            scenario("should allow deleting entities") {
                val subscription = generateSubscription("Gluggle One")
                subscriptionRepository.save(subscription)
                subscriptionRepository.delete(subscription)
                subscriptionRepository.findById(subscription.id!!).shouldBeNull()
            }

            scenario("should throw exception if id not autogenerated") {
                val subscription = SubscriptionModel(
                    name = "WeTube Premium",
                    price = BigDecimal.valueOf(5.50),
                    billingDate = LocalDate.now(),
                    expirationDate = null,
                    renewalPeriod = Period.ofMonths(1),
                    startDate = LocalDate.now(),
                    status = Status.ACTIVE,
                    id = 12357
                )
                shouldThrow<EntityExistsException> {
                    subscriptionRepository.save(subscription)
                }
            }
        }
    }
)

