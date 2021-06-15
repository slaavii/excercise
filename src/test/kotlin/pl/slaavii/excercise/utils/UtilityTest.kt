package pl.slaavii.excercise.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.web.reactive.function.client.WebClient
import pl.slaavii.excercise.models.ClientResponse
import pl.slaavii.excercise.models.Type

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UtilityTest {

    private val utility = Utility(WebClient.create())

    @Test
    fun isDoAskCalculatingOk() {
        val result = utility.doAsk(1000f, 10f, Type.PLN)
        val expected = ClientResponse("PLN", "100,00", "2,00")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun isDoBidCalculatingOk() {
        val result = utility.doBid(10f, 10f, Type.PLN)
        val expected = ClientResponse("PLN", "100,00", "2,00")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun doBidAndAsk() {
        val result = utility.doBidThenAsk(10f, 10f, 10f, Type.PLN)
        val expected = ClientResponse("PLN", "10,00", "0,20")
        assertThat(result).isEqualTo(expected)
    }
}