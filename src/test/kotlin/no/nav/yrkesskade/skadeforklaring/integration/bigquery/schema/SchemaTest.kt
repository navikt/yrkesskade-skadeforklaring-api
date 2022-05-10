package no.nav.yrkesskade.skadeforklaring.integration.bigquery.schema

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.Spraak
import no.nav.yrkesskade.skadeforklaring.metric.SkadeforklaringMetrikkPayload
import no.nav.yrkesskade.skadeforklaring.metric.Vedleggtype
import no.nav.yrkesskade.skadeforklaring.metric.VedleggtypeOgAntall
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.reflect.full.memberProperties

internal class SchemaTest {

    private val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    internal fun `payload mappes riktig til en skadeforklaring_v1 row`() {
        val payload = SkadeforklaringMetrikkPayload(
            kilde = "digital",
            tidspunktMottatt = Instant.now(),
            spraak = Spraak.NB.toString(),
            callId = "callId",
            innmelderrolle = "foresatt",
            antallVedleggTotalt = 3,
            antallVedleggPerType = listOf(
                VedleggtypeOgAntall(Vedleggtype.PDF, 2),
                VedleggtypeOgAntall(Vedleggtype.JPEG, 1),
            )
        )

        val jsonNode = objectMapper.valueToTree<JsonNode>(payload)
        val content = skadeforklaring_v1.transform(jsonNode).content
        assertThat(content["kilde"]).isEqualTo(payload.kilde)
        assertThat(content["tidspunktMottatt"]).isEqualTo(payload.tidspunktMottatt.toString())
        assertThat(content["spraak"]).isEqualTo(payload.spraak)
        assertThat(content["callId"]).isEqualTo(payload.callId)
        assertThat(content["innmelderrolle"]).isEqualTo(payload.innmelderrolle)
        assertThat(content["antallVedleggTotalt"]).isEqualTo(payload.antallVedleggTotalt)
        val expectedAntallVedleggPerType = "[{vedleggtype=PDF, antall=2}, {vedleggtype=JPEG, antall=1}]"
        assertThat(content["antallVedleggPerType"].toString()).isEqualTo(expectedAntallVedleggPerType)
    }

    @Test
    internal fun `skadeforklaring_v1 schema skal ha samme antall felter som payload `() {
        assertThat(SkadeforklaringMetrikkPayload::class.memberProperties.size)
            .isEqualTo(skadeforklaring_v1.define().fields.size)
    }

}