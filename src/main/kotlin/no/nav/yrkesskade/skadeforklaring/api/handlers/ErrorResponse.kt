package no.nav.yrkesskade.skadeforklaring.api.handlers

import java.time.Instant

data class ErrorResponse(val melding: String, val tidspunkt: Instant, val statuskode: Int) {
    companion object {
        fun fraException(exception: Throwable, statuskode: Int) = ErrorResponse(exception.message.orEmpty(), Instant.now(), statuskode)
    }
}