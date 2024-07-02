package app.kerrlab.subspace.core

sealed class SubscriptionError {
    data object NotFound : SubscriptionError()
    data class InvalidData(val message: String) : SubscriptionError()
    data object AlreadyExists : SubscriptionError()
    data class UnknownError(val error: String) : SubscriptionError()
}