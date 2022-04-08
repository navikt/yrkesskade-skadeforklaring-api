package no.nav.yrkesskade.skadeforklaring.integration.pdl

import no.nav.yrkesskade.skadeforklaring.integration.pdl.model.Person

interface IPdlClient {
    fun hentPersonMedForeldreansvar(fodselsnummer: String): Person?
}