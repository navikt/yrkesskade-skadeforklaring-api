package no.nav.yrkesskade.skadeforklaring.vedlegg

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.treeToValue
import no.nav.yrkesskade.skadeforklaring.metric.Vedleggtype
import no.nav.yrkesskade.skadeforklaring.metric.VedleggtypeOgAntall
import no.nav.yrkesskade.skadeforklaring.model.Vedleggreferanse

object VedleggHelper {

    fun tellAntallVedleggPerType(vedleggreferanser: List<Vedleggreferanse>): List<VedleggtypeOgAntall> {
        if (vedleggreferanser.isEmpty()) {
            return emptyList()
        }

        val map = mutableMapOf<Vedleggtype,Int>()

        vedleggreferanser.forEach { ref ->
            val filSuffix = ref.navn.substringAfterLast('.')
            val vedleggtype = Vedleggtype.utled(filSuffix)
            leggTil(vedleggtype, map)
        }

        return map.map { VedleggtypeOgAntall(it.key, it.value) }.toList().sortedBy { it.vedleggtype }
    }

    private fun leggTil(vedleggtype: Vedleggtype, map: MutableMap<Vedleggtype, Int>) {
        if (map.containsKey(vedleggtype)) {
            val antall = map[vedleggtype]!!
            map[vedleggtype] = antall + 1
        } else {
            map[vedleggtype] = 1
        }
    }

    val jsonMapper: JsonMapper = jacksonMapperBuilder()
        .addModule(JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()

    inline fun <reified T> JsonNode.asObject(): T = jsonMapper.treeToValue(this)

    fun JsonNode.asMap(): Map<String, Any?> = asObject()

}