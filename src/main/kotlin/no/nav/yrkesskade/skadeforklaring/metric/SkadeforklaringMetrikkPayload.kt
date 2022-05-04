package no.nav.yrkesskade.skadeforklaring.metric

import java.time.Instant

data class SkadeforklaringMetrikkPayload(
    val kilde: String,
    val tidspunktMottatt: Instant,
    val spraak: String,
    val callId: String,
    val innmelderrolle: String,
    val antallVedleggTotalt: Int,
    val antallVedleggPerType: List<VedleggtypeOgAntall>,
)

data class VedleggtypeOgAntall(
    val vedleggtype: Vedleggtype,
    val antall: Int
)

enum class Vedleggtype {
    PDF,
    JPEG,
    PNG,
    ANNET;

    companion object {

        fun utled(filSuffix: String?): Vedleggtype =
            when (filSuffix?.lowercase()) {
                "pdf" -> PDF
                "jpg", "jpeg" -> JPEG
                "png" -> PNG
                else -> ANNET
            }

    }
}
