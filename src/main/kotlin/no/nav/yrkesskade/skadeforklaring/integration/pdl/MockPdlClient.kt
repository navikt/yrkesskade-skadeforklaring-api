package no.nav.yrkesskade.skadeforklaring.integration.pdl

import no.nav.yrkesskade.skadeforklaring.integration.pdl.model.Person
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalUnit

@Component
@Qualifier("PdlClient")
@ConditionalOnProperty(name = ["service.mock"], havingValue = "true", matchIfMissing = false)
class MockPdlClient() : IPdlClient {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("DD-MM-YYYY")

    /**
     * Henter person med relasjon til personer som denne personen har foreldreansvar for.
     */
    override fun hentPersonMedForeldreansvar(fodselsnummer: String): Person? = Person(
        identifikator = fodselsnummer,
        navn = "Andre McMock",
        foedselsdato = "26-04-1986",
        foedselsaar = 1986,
        foreldreansvar = hentBarn()
    )

    fun hentBarn(): List<Person> = listOf(
        Person(
            identifikator = "01234567891",
            navn = "Nord McMock",
            foedselsdato = "01-01-2010",
            foedselsaar = 2010,
            foreldreansvar = null
        ),
        Person(
            identifikator = "23456789101",
            navn = "Vest McMock",
            foedselsaar = lagFoedselsdato(6).year,
            foedselsdato = lagFoedselsdato(6).format(formatter),
            foreldreansvar = null
        ),
        Person(
            identifikator = "23456789101",
            navn = "Baby McMock",
            foedselsaar = lagFoedselsdato(1).year,
            foedselsdato = lagFoedselsdato(1).format(formatter),
            foreldreansvar = null
        )
    )

    fun lagFoedselsdato(minusAar: Long) = ZonedDateTime.now().minusYears(minusAar)
}