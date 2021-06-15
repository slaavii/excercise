package pl.slaavii.excercise.models

data class ClientRequest(
    val inputCurrency: Type,
    val outputCurrency: Type,
    val amount: Float
)


