package app.kerrlab.subspace.util

import app.kerrlab.subspace.adapters.web.model.CreateSubscriptionDto
import app.kerrlab.subspace.core.Subscription
import app.kerrlab.subspace.core.SubscriptionStatus
import net.datafaker.Faker
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

private val faker = Faker()

fun randomSubscription() = Subscription(
    id = faker.number().randomNumber(4),
    name = faker.word().adjective().replaceFirstChar { it.uppercaseChar() }
            + " "
            + faker.dungeonsAndDragons().klasses(),
    price = faker.number().numberBetween(0, 100).toBigDecimal(),
    status = SubscriptionStatus.entries.random(),
    billingDate = LocalDate.ofInstant(faker.timeAndDate().future(365, TimeUnit.DAYS), ZoneOffset.UTC),
    startDate = LocalDate.ofInstant(faker.timeAndDate().future(365, TimeUnit.DAYS), ZoneOffset.UTC),
    endDate = LocalDate.ofInstant(faker.timeAndDate().future(365, 30, TimeUnit.DAYS), ZoneOffset.UTC),
    renewalPeriod = Period.ofDays(faker.number().numberBetween(0, 60)),
)

fun randomSubscriptionRequest(): CreateSubscriptionDto {
    val subscription = randomSubscription()
    return CreateSubscriptionDto(
        name = subscription.name,
        price = subscription.price,
        status = subscription.status,
        billingDate = subscription.billingDate,
        startDate = subscription.startDate,
        endDate = subscription.endDate,
    )
}