package no.nav.yrkesskade.skadeforklaring.metric

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VedleggtypeTest {

    @Test
    fun `skal utlede vedleggtype`() {
        assertThat(Vedleggtype.utled("pdf")).isEqualTo(Vedleggtype.PDF)
        assertThat(Vedleggtype.utled("JpG")).isEqualTo(Vedleggtype.JPEG)
        assertThat(Vedleggtype.utled("jpeg")).isEqualTo(Vedleggtype.JPEG)
        assertThat(Vedleggtype.utled("PNG")).isEqualTo(Vedleggtype.PNG)
        assertThat(Vedleggtype.utled("foobar")).isEqualTo(Vedleggtype.ANNET)
        assertThat(Vedleggtype.utled(null)).isEqualTo(Vedleggtype.ANNET)
    }

}