package no.nav.yrkesskade.skadeforklaring.model

data class Fravaer(
    val harFravaer: Boolean,
    val fravaertype: String,
    val antallDager: Int,
)