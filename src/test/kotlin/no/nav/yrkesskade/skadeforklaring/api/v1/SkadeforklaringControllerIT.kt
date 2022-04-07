package no.nav.yrkesskade.skadeforklaring.api.v1

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import no.nav.yrkesskade.skadeforklaring.model.Behandler
import no.nav.yrkesskade.skadeforklaring.model.Fravaer
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.model.Tid
import no.nav.yrkesskade.skadeforklaring.security.TokenService
import no.nav.yrkesskade.skadeforklaring.services.BrukerService
import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import no.nav.yrkesskade.skadeforklaring.test.fixtures.getEnkelskadeforklaring
import no.nav.yrkesskade.skadeforklaring.test.fixtures.getEnkelskadeforklaringMedFeilPostnummer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant

private const val SKADEFORKLARING_PATH = "/v1/skadeforklaringer"

@AutoConfigureMockMvc
@SpringBootTest
class SkadeforklaringControllerIT : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `send skadeforklaring - autentisert`() {
        val skadeforklaring = getEnkelskadeforklaring()

        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
        val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

        postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `send skadeforklaring med bokstav i postnummer - autentisert`() {
        val skadeforklaring = getEnkelskadeforklaringMedFeilPostnummer()

        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
        val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

        postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().is5xxServerError)
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