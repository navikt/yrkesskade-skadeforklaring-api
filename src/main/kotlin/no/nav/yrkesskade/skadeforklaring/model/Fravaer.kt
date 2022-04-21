package no.nav.yrkesskade.skadeforklaring.model

import io.swagger.v3.oas.annotations.media.Schema

data class Fravaer(
    @Schema(example = "treDagerEllerMindre", description = "Beskriver om det har vært fravær i forbindelse med skaden. Gyldige verdier hentes fra kodeverktjenesten for typen 'foerteDinSkadeEllerSykdomTilFravaer'")
    val foerteDinSkadeEllerSykdomTilFravaer: String,
    @Schema(example = "sykemelding", description = "Beskriver hvilken type fravær det har vært i forbindelse med skaden. Gyldige verdier hentes fra kodeverktjenesten for typen 'fravaertype'")
    val fravaertype: String?,
)