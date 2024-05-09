package app.kerrlab.subspace.domain.model

sealed class SubscriptionError {
    data object NotFound : SubscriptionError()
    data class InvalidData(val message: String) : SubscriptionError()
    data object AlreadyExists : SubscriptionError()
}