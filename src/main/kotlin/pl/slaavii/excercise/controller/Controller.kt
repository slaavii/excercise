package pl.slaavii.excercise.controller

import org.springframework.web.bind.annotation.*
import pl.slaavii.excercise.models.ClientRequest
import pl.slaavii.excercise.models.ClientResponse
import pl.slaavii.excercise.models.ResponseFromNbpApi
import pl.slaavii.excercise.utils.Utility

@RestController
class Controller(val utility: Utility) {

    @GetMapping("/getExchange/{currency}")
    fun getAllExchange(@PathVariable currency: String): ResponseFromNbpApi? = utility.getExchangeRate(currency)

    @PostMapping("/doExchange")
    fun doExchange(@RequestBody clientRequest: ClientRequest): ClientResponse = utility.doExchange(clientRequest)
}