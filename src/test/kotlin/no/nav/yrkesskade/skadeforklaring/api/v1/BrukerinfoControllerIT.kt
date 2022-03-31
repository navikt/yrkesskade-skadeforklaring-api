package no.nav.yrkesskade.skadeforklaring.api.v1

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

private const val USER_INFO_PATH = "/v1/brukerinfo"

@AutoConfigureMockMvc
class BrukerinfoControllerIT : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `hent brukerinfo - autentisert`() {
        // gyldig JWT
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

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