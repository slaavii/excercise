package pl.slaavii.excercise.models

data class ClientResponse(
    val currency: String,
    val amount: String = "0",
    val commision: String = "0"
)