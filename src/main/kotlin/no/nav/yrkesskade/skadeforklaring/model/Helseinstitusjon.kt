package no.nav.yrkesskade.skadeforklaring.model

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Max

data class Helseinstitusjon(
    @Schema(example = "ja", description = "Beskriver om skadelidt har vært hos medisinsk behandler. Gyldige verdier er 'ja' eller 'nei'")
    val erHelsepersonellOppsokt: String,
    @Max(150)
    val navn: String?,
    val adresse: Adresse?
)