package no.nav.yrkesskade.skadeforklaring.metric

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VedleggtypeOgAntallTest {

    @Test
    fun `skal sortere paa vedleggtype i rekkefølgen de er definert i enum`() {
        val list = listOf(
            VedleggtypeOgAntall(Vedleggtype.JPEG, 2),
            VedleggtypeOgAntall(Vedleggtype.ANNET, 1),
            VedleggtypeOgAntall(Vedleggtype.PNG, 1),
            VedleggtypeOgAntall(Vedleggtype.PDF, 5),
        )

        val sorted = list.sortedBy { it.vedleggtype }.map { it.vedleggtype }.toList()

        assertThat(sorted).containsSequence(Vedleggtype.PDF, Vedleggtype.JPEG, Vedleggtype.PNG, Vedleggtype.ANNET)
    }
}