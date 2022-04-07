package no.nav.yrkesskade.skadeforklaring.api.v1

import com.expediagroup.graphql.client.spring.GraphQLWebClient
import io.mockk.MockKSettings.relaxed
import io.mockk.every
import io.mockk.mockk
import no.nav.yrkesskade.skadeforklaring.model.Brukerinfo
import no.nav.yrkesskade.skadeforklaring.model.Person
import no.nav.yrkesskade.skadeforklaring.services.BrukerService
import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import no.nav.yrkesskade.skadeforklaring.test.TestMockServerInitialization
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
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
        fun brukerService() = mockk<BrukerService>(relaxed = true)
    }

    @Autowired
    lateinit var brukerService: BrukerService

    @Test
    fun `hent brukerinfo - autentisert`() {
        // gyldig JWT
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        val brukerinfo = Brukerinfo("12345678910", "Ola Testesen", "01-01-1970", listOf(
            Person("0123456789", "Hege Testesen", "01-01-2000", 2000)        ))

        every { brukerService.hentBrukerinfo(any()) } returns brukerinfo

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.get(USER_INFO_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.identifikator").value("12345678910"))
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