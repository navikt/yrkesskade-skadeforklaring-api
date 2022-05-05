package no.nav.yrkesskade.skadeforklaring.integration.bigquery.schema

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.google.cloud.bigquery.InsertAllRequest
import com.google.cloud.bigquery.Schema
import no.nav.yrkesskade.skadeforklaring.metric.SkadeforklaringMetrikkPayload
import no.nav.yrkesskade.skadeforklaring.vedlegg.VedleggHelper.asMap

val skadeforklaring_v1 = object : SchemaDefinition {

    override val schemaId: SchemaId = SchemaId(name = "skadeforklaring_api", version = 1)
    val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun define(): Schema = schema {
        string("kilde") {
            required()
            description("Systemet som sendte skadeforklaringen")
        }
        timestamp("tidspunktMottatt") {
            required()
            description("Tidspunkt da skadeforklaringen ble mottatt")
        }
        string("spraak") {
            required()
            description("Skadeforklaringens språk")
        }
        string("callId") {
            required()
            description("Unik ID for innmeldingens systemtransaksjon")
        }
        string("innmelderrolle") {
            required()
            description("Innmelders rolle")
        }
        int("antallVedleggTotalt") {
            required()
            description("Totalt antall vedlegg sendt inn sammen med skadeforklaringen")
        }
        struct("antallVedleggPerType") {
            repeated()
            description("Antall vedlegg per vedleggtype sendt inn sammen med skadeforklaringen")
            subFields {
                string("vedleggtype") {
                    required()
                }
                int("antall") {
                    required()
                }
            }
        }
    }

    override fun transform(payload: JsonNode): InsertAllRequest.RowToInsert {
        val payloadMap = payload.asMap()
        val skademeldingPayload = objectMapper.treeToValue<SkadeforklaringMetrikkPayload>(payload)
        return InsertAllRequest.RowToInsert.of(
            mapOf(
                "kilde" to skademeldingPayload.kilde,
                "tidspunktMottatt" to skademeldingPayload.tidspunktMottatt.toString(),
                "spraak" to skademeldingPayload.spraak,
                "callId" to skademeldingPayload.callId,
                "innmelderrolle" to skademeldingPayload.innmelderrolle,
                "antallVedleggTotalt" to skademeldingPayload.antallVedleggTotalt,
                "antallVedleggPerType" to payloadMap["antallVedleggPerType"]
            )
        )
    }
}