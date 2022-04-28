package no.nav.yrkesskade.skadeforklaring.services

import no.nav.yrkesskade.skadeforklaring.integration.pdl.IPdlClient
import no.nav.yrkesskade.skadeforklaring.model.Brukerinfo
import no.nav.yrkesskade.skadeforklaring.model.Person
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

const val SKOLE_ALDER = 5;

@Service
class BrukerService constructor(@Qualifier("PdlClient") val pdlClient: IPdlClient) {

    fun hentBrukerinfo(fodselsnummer: String): Brukerinfo {
        val person = pdlClient.hentPersonMedForeldreansvar(fodselsnummer)
        if (person == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person finnes ikke i personregisteret")
        }
        return Brukerinfo(
            identifikator = fodselsnummer,
            navn = person.navn,
            fodselsdato = person.foedselsdato,
            foreldreansvar = person.foreldreansvar?.filter {
                erSkoleAlder(it.foedselsaar)
            }?.map {
                Person(
                    identifikator = it.identifikator,
                    foedselsaar = it.foedselsaar,
                    foedselsdato = it.foedselsdato,
                    navn = it.navn
                )
            }.orEmpty()
        )
    }

    private fun erSkoleAlder(fodselsaar: Int): Boolean {
        val differanse = Calendar.getInstance().get(Calendar.YEAR) - fodselsaar

        return differanse >= SKOLE_ALDER;
    }
}