package no.nav.yrkesskade.skadeforklaring.integration.pdl

import com.expediagroup.graphql.client.spring.GraphQLWebClient
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersonMedForeldreansvar
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersoner
import no.nav.yrkesskade.skadeforklaring.security.TokenService
import no.nav.yrkesskade.skadeforklaring.test.fixtures.okResponsPersonMedForeldreansvarFraPdl
import no.nav.yrkesskade.skadeforklaring.test.fixtures.okResponsPersonerFraPdl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(MockKExtension::class)
class PdlClientTest {

    private lateinit var client: PdlClient

    @MockK
    lateinit var graphQLWebClient: GraphQLWebClient

    @MockK(relaxed = true)
    lateinit var tokenUtilMock: TokenService


    @BeforeEach
    fun init() {
        every { tokenUtilMock.getAppAccessTokenWithScope(any()) } returns "abc"

        client = PdlClient(pdlGraphqlUrl = "test", tokenUtilMock)
        ReflectionTestUtils.setField(client, "client", graphQLWebClient)
    }

    @Test
    fun `skal hente person med foreldre ansvar`() {
        coEvery { graphQLWebClient.execute(ofType(HentPersonMedForeldreansvar::class), any()) } returns okResponsPersonMedForeldreansvarFraPdl()
        coEvery { graphQLWebClient.execute(ofType(HentPersoner::class), any()) } returns okResponsPersonerFraPdl()

        val person = client.hentPersonMedForeldreansvar("12345678910")

        assertThat(person?.navn).isEqualTo("Ola Normann")
        val foreldreansvar = person?.foreldreansvar
        assertThat(foreldreansvar?.size).isEqualTo(2)
    }

}