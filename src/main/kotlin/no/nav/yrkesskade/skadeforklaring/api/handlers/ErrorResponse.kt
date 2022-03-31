package no.nav.yrkesskade.skadeforklaring.api.handlers

import java.time.Instant

data class ErrorResponse(val melding: String, val tidspunkt: Instant, val statuskode: Int)