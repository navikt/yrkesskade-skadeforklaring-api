package no.nav.yrkesskade.skadeforklaring.api.v1

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import no.nav.yrkesskade.skadeforklaring.test.fixtures.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

private const val SKADEFORKLARING_PATH = "/v1/skadeforklaringer"

@AutoConfigureMockMvc
@SpringBootTest
class SkadeforklaringControllerIT : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `send skadeforklaring - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaring()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().isCreated)

            i++
        }
    }

    @Test
    fun `send skadeforklaring med ugyldig fravaertype - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaringUgyldigFravaertype()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().is4xxClientError)

            i++
        }
    }

    @Test
    fun `send skadeforklaring uten fravaer - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaringIngenFravaer()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().isCreated)

            i++
        }
    }

    @Test
    fun `send skadeforklaring -fravaer ikke relevant - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaringOenskerIkkeOppgiFravaer()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().isCreated)

            i++
        }
    }

    @Test
    fun `send skadeforklaring - uten skadelidts identitetsnummer - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaringUtenSkadelidtsIdentitetsnummer()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().is4xxClientError)

            i++
        }
    }

    @Test
    fun `send skadeforklaring - uten innmelders identitetsnummer - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaringUtenInnmeldersIdentitetsnummer()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().is4xxClientError)

            i++
        }
    }

    @Test
    fun `send skadeforklaring med helsepersonell er oppsoekt - autentisert`() {
        var i = 0
        while (i < 10) {
            val skadeforklaring = getEnkelskadeforklaringMedHelsepersonellOppsoekt()

            val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
            val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

            postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().isCreated)

            i++
        }
    }

    private fun postSkadeforklaring(skadeforklaring: String, token: String) =
        mvc.perform(
            MockMvcRequestBuilders.post(SKADEFORKLARING_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
                .content(skadeforklaring)
        ).andDo(MockMvcResultHandlers.print())

    private fun skadeforklaringTilString(skadeforklaring: Skadeforklaring): String =
        objectMapper.writeValueAsString(skadeforklaring)
}