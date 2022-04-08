package no.nav.yrkesskade.skadeforklaring.services

import no.nav.yrkesskade.skadeforklaring.integration.pdl.PdlClient
import no.nav.yrkesskade.skadeforklaring.model.Brukerinfo
import no.nav.yrkesskade.skadeforklaring.model.Person
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class BrukerService(private val pdlClient: PdlClient) {

    fun hentBrukerinfo(fodselsnummer: String): Brukerinfo {
        val person = pdlClient.hentPersonMedForeldreansvar(fodselsnummer)
        if (person == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person finnes ikke i personregisteret")
        }
        return Brukerinfo(
            identifikator = fodselsnummer,
            navn = person.navn,
            fodselsdato = person.foedselsdato,
            foreldreansvar = person.foreldreansvar?.map {
                Person(
                    identifikator = it.identifikator,
                    foedselsaar = it.foedselsaar,
                    foedselsdato = it.foedselsdato,
                    navn = it.navn
                )
            }.orEmpty()
        )
    }
}