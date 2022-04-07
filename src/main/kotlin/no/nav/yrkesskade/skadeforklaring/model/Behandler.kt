package no.nav.yrkesskade.skadeforklaring.model

data class Behandler(
    val erBehandlerOppsokt: Boolean,
    val behandlerNavn: String?,
    val adresse: Adresse?
)