package no.nav.yrkesskade.skadeforklaring.model

import java.time.Instant

data class Tid(
    val tidstype: String,
    val tidspunkt: Instant?,
    val periode: Periode?
)

data class Periode(
    val fra: Instant,
    val til: Instant,
)