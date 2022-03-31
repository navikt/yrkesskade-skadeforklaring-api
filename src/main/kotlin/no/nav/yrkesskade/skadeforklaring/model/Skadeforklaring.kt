package no.nav.yrkesskade.skadeforklaring.model

data class Skadeforklaring(
    val identifikator: String,
    val arbeidsbeskrivelse: String,
    val ulykkesbeskrivelse: String,
    val tid: Tid,
    val vedleggtype: String,
    val vedleggreferanser: List<Vedleggreferanse>,
    val fravaer: Fravaer,
    val behandler: Behandler
)