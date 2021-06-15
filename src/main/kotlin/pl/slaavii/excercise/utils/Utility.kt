package pl.slaavii.excercise.utils

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import pl.slaavii.excercise.models.ClientRequest
import pl.slaavii.excercise.models.ClientResponse
import pl.slaavii.excercise.models.ResponseFromNbpApi
import pl.slaavii.excercise.models.Type

@Component
class Utility(val webClient: WebClient) {
    fun getExchangeRate(currency: String): ResponseFromNbpApi? = webClient.get()
        .uri { it.pathSegment("api", "exchangerates", "rates", "C", currency).build() }
        .retrieve()
        .bodyToMono(ResponseFromNbpApi::class.java)
        .block()

    fun doExchange(clientRequest: ClientRequest): ClientResponse {
        when {
            clientRequest.amount <= 0 -> return ClientResponse("amount must be greater than 0")
            clientRequest.inputCurrency == clientRequest.outputCurrency -> return ClientResponse("invalid currency")
            clientRequest.inputCurrency == Type.PLN -> {
                val exchangeRate = getExchangeRate(clientRequest.outputCurrency.currency)
                return doAsk(
                    clientRequest.amount,
                    exchangeRate?.rates?.get(0)?.ask ?: 1f,
                    clientRequest.outputCurrency
                )
            }
            clientRequest.outputCurrency == Type.PLN -> {
                val exchangeRate = getExchangeRate(clientRequest.inputCurrency.currency)
                return doBid(
                    clientRequest.amount,
                    exchangeRate?.rates?.get(0)?.bid ?: 1f,
                    clientRequest.outputCurrency
                )
            }
            else -> {
                val exchangeRateIn = getExchangeRate(clientRequest.inputCurrency.currency)
                val exchangeRateOut = getExchangeRate(clientRequest.outputCurrency.currency)
                return doBidThenAsk(
                    clientRequest.amount,
                    exchangeRateIn?.rates?.get(0)?.bid ?: 1f,
                    exchangeRateOut?.rates?.get(0)?.ask ?: 1f,
                    clientRequest.outputCurrency
                )
            }
        }
    }

    fun doAsk(amount: Float, converter: Float, currency: Type): ClientResponse {
        val value = amount / converter
        return ClientResponse(currency.currency, "%.2f".format(value), "%.2f".format(value * 0.02))
    }

    fun doBid(amount: Float, converter: Float, currency: Type): ClientResponse {
        val value = amount * converter
        return ClientResponse(currency.currency, "%.2f".format(value), "%.2f".format(value * 0.02))
    }

    fun doBidThenAsk(amount: Float, converterIn: Float, converterOut: Float, currency: Type): ClientResponse {
        val value = (amount * converterIn) / converterOut
        return ClientResponse(currency.currency, "%.2f".format(value), "%.2f".format(value * 0.02))
    }
}