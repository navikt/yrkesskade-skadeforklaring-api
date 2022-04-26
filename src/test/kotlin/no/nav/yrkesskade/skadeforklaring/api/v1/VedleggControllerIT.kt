package no.nav.yrkesskade.skadeforklaring.api.v1

import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.mock.web.MockPart
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


private const val VEDLEGG_PATH = "/v1/vedlegg"

@AutoConfigureMockMvc
class VedleggControllerIT : AbstractTest() {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `last opp vedlegg - autentisert`() {
        // gyldig JWT
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        val mockMultipartFile = MockMultipartFile(
            "vedlegg", "test.pdf",
            "application/pdf", "test data".toByteArray()
        )

        val part = MockPart("id", "test".toByteArray(Charsets.UTF_8))

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.multipart(VEDLEGG_PATH)
                .file(mockMultipartFile)
                .part(part)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding(Charsets.UTF_8)

        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.redirectedUrl("/blob/test"))
    }

    @Test
    fun `hent vedlegg som finnes - autentisert`() {
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        val mockMultipartFile = MockMultipartFile(
            "vedlegg", "test.pdf",
            "application/pdf", "test data".toByteArray()
        )

        val bytesTilHenting = "til_henting".toByteArray(Charsets.UTF_8)
        val part = MockPart("id", bytesTilHenting)

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.multipart(VEDLEGG_PATH)
                .file(mockMultipartFile)
                .part(part)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding(Charsets.UTF_8)

        )

        mvc.perform(
            MockMvcRequestBuilders.get("$VEDLEGG_PATH/{id}", "til_henting")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(containsString("test.pdf")))
    }

    @Test
    fun `hent vedlegg som ikke finnes - autentisert`() {
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        mvc.perform(
            MockMvcRequestBuilders.get("$VEDLEGG_PATH/{id}", "finnes_ikke")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `slett vedlegg som finnes - autentisert`() {
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        val mockMultipartFile = MockMultipartFile(
            "vedlegg", "test.pdf",
            "application/pdf", "test data".toByteArray()
        )

        val part = MockPart("id", "til_sletting".toByteArray(Charsets.UTF_8))

        // Data for eksterne tjenester kommer fra localhost MockServer
        mvc.perform(
            MockMvcRequestBuilders.multipart(VEDLEGG_PATH)
                .file(mockMultipartFile)
                .part(part)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding(Charsets.UTF_8)

        )

        mvc.perform(
            MockMvcRequestBuilders.delete("$VEDLEGG_PATH/{id}", "til_sletting")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)

    }

    @Test
    fun `slett vedlegg som ikke finnes - autentisert`() {
        val jwt = mvc.perform(MockMvcRequestBuilders.get("/local/jwt")).andReturn().response.contentAsString

        mvc.perform(
            MockMvcRequestBuilders.delete("$VEDLEGG_PATH/{id}", "finnes_ikke")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }
}