package no.nav.yrkesskade.skadeforklaring.integration.virusskanner.model

import com.fasterxml.jackson.annotation.JsonAlias

enum class Result {
    FOUND, OK
}

data class ScanResult(
    @JsonAlias("Filename")
    val filename: String,
    @JsonAlias("Result")
    val result: Result
)