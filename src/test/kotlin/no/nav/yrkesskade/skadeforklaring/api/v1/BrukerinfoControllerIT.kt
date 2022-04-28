package no.nav.yrkesskade.skadeforklaring.api.v1

import io.mockk.every
import io.mockk.mockk
import no.nav.yrkesskade.skadeforklaring.integration.pdl.PdlClient
import no.nav.yrkesskade.skadeforklaring.integration.pdl.model.Person
import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import no.nav.yrkesskade.skadeforklaring.test.TestMockServerInitialization
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

private const val USER_INFO_PATH = "/v1/brukerinfo"

@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [TestMockServerInitialization::class])
class BrukerinfoControllerIT : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun pdlClient() = mockk<PdlClient>(relaxed = true)
    }

    @Autowired
    lateinit var pdlClient: PdlClient

    @Test
    fun `hent brukerinfo - autentisert`() {
        // gyldig JWT
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        val person = Person(
            identifikator = "12345678910",
            navn = "Ola Testesen",
            foedselsdato = "01-01-1970",
            foedselsaar = 1970,
            foreldreansvar = listOf(Person("0123456789", "Hege Testesen", 2000, "01-01-2000", null))
        )

        every { pdlClient.hentPersonMedForeldreansvar(any()) } returns person

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.get(USER_INFO_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.identifikator").value("12345678910"))
            .andExpect(jsonPath("$.foreldreansvar.length()").value(1))
    }

    @Test
    fun `hent brukerinfo, barn ikke i skolealder - autentisert`() {
        // gyldig JWT
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        val fodselsaar = Calendar.getInstance().get(Calendar.YEAR);
        val person = Person(
            identifikator = "12345678910",
            navn = "Ola Testesen",
            foedselsdato = "01-01-1970",
            foedselsaar = 1970,
            foreldreansvar = listOf(Person("0123456789", "Hege Testesen", fodselsaar, "01-01-$fodselsaar", null))
        )

        every { pdlClient.hentPersonMedForeldreansvar(any()) } returns person

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.get(USER_INFO_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.identifikator").value("12345678910"))
            .andExpect(jsonPath("$.foreldreansvar").isEmpty)
    }

    @Test
    fun `hent brukerinfo - uautorisert`() {

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.get(USER_INFO_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
        ).andExpect(MockMvcResultMatchers.status().is5xxServerError)
    }

}