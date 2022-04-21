package no.nav.yrkesskade.skadeforklaring.model

import io.swagger.v3.oas.annotations.media.Schema

data class Behandler(
    @Schema(example = "ja", description = "Beskriver om skadelidt har vært hos medisinsk behandler. Gyldige verdier er 'ja' eller 'nei'")
    val erBehandlerOppsokt: String,
    val behandlerNavn: String?,
    val adresse: Adresse?
)