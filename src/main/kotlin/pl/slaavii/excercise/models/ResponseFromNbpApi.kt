package pl.slaavii.excercise.models

data class ResponseFromNbpApi(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<Currency>
) {
    data class Currency(
        val no: String,
        val effectiveDate: String,
        val bid: Float,
        val ask: Float
    )
}