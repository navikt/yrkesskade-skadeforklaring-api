package no.nav.yrkesskade.skadeforklaring.metric

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.yrkesskade.skadeforklaring.integration.bigquery.BigQueryClient
import no.nav.yrkesskade.skadeforklaring.integration.bigquery.schema.skadeforklaring_v1
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringInnsendingHendelse
import no.nav.yrkesskade.skadeforklaring.vedlegg.VedleggHelper
import org.springframework.stereotype.Service

@Service
class MetricService(
    private val metrikkClient: BigQueryClient
) {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun insertMetrikk(record: SkadeforklaringInnsendingHendelse) {
        val skadeforklaringMetrikkPayload = SkadeforklaringMetrikkPayload(
            kilde = "ukjent",
            tidspunktMottatt = record.metadata.tidspunktMottatt,
            spraak = record.metadata.spraak.toString(),
            callId = record.metadata.navCallId,
            innmelderrolle = record.skadeforklaring.innmelder?.innmelderrolle?.lowercase() ?: "ukjent",
            antallVedleggTotalt = record.skadeforklaring.vedleggreferanser.size,
            antallVedleggPerType = VedleggHelper.tellAntallVedleggPerType(record.skadeforklaring.vedleggreferanser)
        )

        metrikkClient.insert(
            skadeforklaring_v1,
            skadeforklaring_v1.transform(objectMapper.valueToTree(skadeforklaringMetrikkPayload))
        )
    }

}