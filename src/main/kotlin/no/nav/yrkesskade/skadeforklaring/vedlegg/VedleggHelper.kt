package no.nav.yrkesskade.skadeforklaring.vedlegg

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

}