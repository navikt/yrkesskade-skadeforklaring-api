package no.nav.yrkesskade.skadeforklaring.model

import javax.validation.constraints.Size

data class Skadeforklaring(
    val saksnummer: String?,
    val innmelder: Innmelder,
    val skadelidt: Skadelidt,
    @Size(max = 1000)
    val arbeidetMedIUlykkesoeyeblikket: String,
    @Size(max = 2000)
    val noeyaktigBeskrivelseAvHendelsen: String,
    val tid: Tid,
    val skalEttersendeDokumentasjon: String,
    val vedleggreferanser: List<Vedleggreferanse>,
    val fravaer: Fravaer,
    val helseinstitusjon: Helseinstitusjon
)