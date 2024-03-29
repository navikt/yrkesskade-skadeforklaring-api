package no.nav.yrkesskade.skadeforklaring.integration.pdl

import com.expediagroup.graphql.client.spring.GraphQLWebClient
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import kotlinx.coroutines.runBlocking
import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor
import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor.Companion.CORRELATION_ID_HEADER_NAME
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersonMedForeldreansvar
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersoner
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.Navn
import no.nav.yrkesskade.skadeforklaring.integration.pdl.model.Person
import no.nav.yrkesskade.skadeforklaring.security.TokenService
import no.nav.yrkesskade.skadeforklaring.utils.getLogger
import no.nav.yrkesskade.skadeforklaring.utils.getSecureLogger
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
@Qualifier("PdlClient")
@ConditionalOnProperty(name = ["service.mock"], havingValue = "false", matchIfMissing = true)
class PdlClient(
    @Value("\${integration.client.pdl.url}") private val pdlGraphqlUrl: String,
    private val tokenService: TokenService
): IPdlClient {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = getLogger(javaClass.enclosingClass)
        private val secureLogger = getSecureLogger()
    }

    private val tokenClientName = "pdl"
    private val tokenMaskinTilMaskinClientName = "pdl-maskintilmaskin"

    private val client = GraphQLWebClient(url = pdlGraphqlUrl)

    /**
     * Henter person med relasjon til personer som denne personen har foreldreansvar for.
     */
    override fun hentPersonMedForeldreansvar(fodselsnummer: String): Person? {
        val token = tokenService.getAppAccessTokenWithScope(tokenClientName)
        val hentPersonMedForeldreansvarQuery =
            HentPersonMedForeldreansvar(HentPersonMedForeldreansvar.Variables(fodselsnummer))

        val personResult: HentPersonMedForeldreansvar.Result?
        runBlocking {
            logger.info("Henter person fra PDL på url $pdlGraphqlUrl")
            secureLogger.info("Henter person fra PDL for person med fnr $fodselsnummer på url $pdlGraphqlUrl")
            val response: GraphQLClientResponse<HentPersonMedForeldreansvar.Result> =
                client.execute(hentPersonMedForeldreansvarQuery) {
                    headers {
                        it.add(HttpHeaders.AUTHORIZATION, "Bearer $token")
                        it.add("Tema", "YRK")
                        it.add(
                            CORRELATION_ID_HEADER_NAME, MDC.get(
                                CorrelationInterceptor.CORRELATION_ID_LOG_VAR_NAME
                            )
                        )
                    }
                }
            personResult = response.data
            logger.info("Returnerte fra PDL, se securelogs for detaljer")
            secureLogger.info("Returnerte fra PDL, data: " + response.data)
            if (!response.errors.isNullOrEmpty()) {
                logger.error("Responsen fra PDL inneholder feil! Se securelogs")
                secureLogger.error("Responsen fra PDL inneholder feil: ${response.errors}")
                throw RuntimeException("Responsen fra PDL inneholder feil! Se securelogs")
            }
        }

        val hentetPerson = personResult?.hentPerson;

        if (hentetPerson != null) {
            var subjekter = emptyList<Person>()

            val subjektidliste = hentetPerson.foreldreansvar.filter { it.ansvarssubjekt != null }.map {
                it.ansvarssubjekt
            }

            if (!subjektidliste.isEmpty()) {
                subjekter = hentPersoner(subjektidliste as List<String>)?.hentPersonBolk?.map {
                    val navn = lagNavn(it.person?.navn?.firstOrNull())
                    Person(
                        identifikator = it.ident,
                        navn = navn,
                        foedselsaar = it.person?.foedsel?.first()?.foedselsaar!!,
                        foedselsdato = it.person?.foedsel?.first().foedselsdato.orEmpty(),
                        doedsdato = it.person?.doedsfall?.firstOrNull()?.doedsdato,
                        foreldreansvar = null
                    )
                }!!
            }

            val navn = lagNavn(hentetPerson.navn.firstOrNull())
            return Person(
                identifikator = fodselsnummer,
                navn = navn,
                foedselsaar = hentetPerson.foedsel.first().foedselsaar!!,
                foedselsdato = hentetPerson.foedsel.first().foedselsdato.orEmpty(),
                foreldreansvar = subjekter,
                doedsdato = null
            )
        }

        return null
    }

    private fun lagNavn(navn: no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.Navn?): String {
        if (navn == null) {
            return "Ukjent - ingen navn info fra PDL"
        }

        val navnBuilder = StringBuilder()
        navnBuilder.append(navn.fornavn)
        if (!navn.mellomnavn.isNullOrBlank()) {
            navnBuilder.append(" ").append(navn.mellomnavn)
        }
        navnBuilder.append(" ").append(navn.etternavn)

        return navnBuilder.toString()
    }

    private fun lagNavn(navn: no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersonmedforeldreansvar.Navn?): String {
        if (navn == null) {
            return "Ukjent - ingen navn info fra PDL"
        }

        val navnBuilder = StringBuilder()
        navnBuilder.append(navn.fornavn)
        if (!navn.mellomnavn.isNullOrBlank()) {
            navnBuilder.append(" ").append(navn.mellomnavn)
        }
        navnBuilder.append(" ").append(navn.etternavn)

        return navnBuilder.toString()
    }

    /**
     * Hent en bolk med personer
     */
    private fun hentPersoner(fodselsnummerliste: List<String>): HentPersoner.Result? {
        val token = tokenService.getAppAccessTokenWithScope(tokenMaskinTilMaskinClientName)
        val hentPersonerQuery = HentPersoner(HentPersoner.Variables(fodselsnummerliste))

        val personerResult: HentPersoner.Result?
        runBlocking {
            secureLogger.info("Henter person fra PDL for personer med fnr $fodselsnummerliste på url $pdlGraphqlUrl")
            val response: GraphQLClientResponse<HentPersoner.Result> = client.execute(hentPersonerQuery) {
                headers {
                    it.add(HttpHeaders.AUTHORIZATION, "Bearer $token")
                    it.add("Tema", "YRK")
                    it.add(
                        CORRELATION_ID_HEADER_NAME, MDC.get(
                            CorrelationInterceptor.CORRELATION_ID_LOG_VAR_NAME
                        )
                    )
                }
            }
            personerResult = response.data
            logger.info("Returnerte fra PDL, se securelogs for detaljer")
            secureLogger.info("Returnerte fra PDL, data: " + response.data)
            if (!response.errors.isNullOrEmpty()) {
                logger.error("Responsen fra PDL inneholder feil! Se securelogs")
                secureLogger.error("Responsen fra PDL inneholder feil: ${response.errors}")
                throw RuntimeException("Responsen fra PDL inneholder feil! Se securelogs")
            }
        }

        return personerResult
    }
}