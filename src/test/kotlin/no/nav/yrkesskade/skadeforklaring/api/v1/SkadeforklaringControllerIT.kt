package no.nav.yrkesskade.skadeforklaring.api.v1

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.yrkesskade.skadeforklaring.model.Behandler
import no.nav.yrkesskade.skadeforklaring.model.Fravaer
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.model.Tid
import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant

private const val SKADEFORKLARING_PATH = "/v1/skadeforklaringer"

@AutoConfigureMockMvc
class SkadeforklaringControllerIT : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `send skadeforklaring - autentisert`() {
        val skadeforklaring = Skadeforklaring(
            identifikator = "12345678910",
            "En kort arbeidsbeskrivelse",
            "En litt lengre ulykkesbeskrivelse",
            tid = Tid(tidspunkt = Instant.now(), tidstype = "Tidspunkt", periode = null),
            vedleggtype = "Papir",
            vedleggreferanser = emptyList(),
            fravaer = Fravaer(harFravaer = true, fravaertype = "Sykemelding"),
            behandler = Behandler(behandlerNavn = "Test Testesen", erBehandlerOppsokt = true)
        )

        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString
        val skadeforklaringString = skadeforklaringTilString(skadeforklaring);

        postSkadeforklaring(skadeforklaringString, jwt).andExpect(MockMvcResultMatchers.status().isCreated)
    }

    private fun postSkadeforklaring(skadeforklaring: String, token: String) =
        mvc.perform(
            MockMvcRequestBuilders.post(SKADEFORKLARING_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
                .content(skadeforklaring)
        )

    private fun skadeforklaringTilString(skadeforklaring: Skadeforklaring): String =
        objectMapper.writeValueAsString(skadeforklaring)
}