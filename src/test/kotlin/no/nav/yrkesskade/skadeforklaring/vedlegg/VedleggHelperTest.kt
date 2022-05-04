package no.nav.yrkesskade.skadeforklaring.vedlegg

import no.nav.yrkesskade.skadeforklaring.metric.Vedleggtype
import no.nav.yrkesskade.skadeforklaring.metric.VedleggtypeOgAntall
import no.nav.yrkesskade.skadeforklaring.model.Vedleggreferanse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VedleggHelperTest {

    @Test
    fun `skal telle antall vedlegg per vedleggtype`() {
        val vedleggreferanser = listOf(
            Vedleggreferanse("11", "vedlegg1.JpG", 100L, "en/url/"),
            Vedleggreferanse("19", "vedlegg2.jpeg", 100L, "en/url/"),
            Vedleggreferanse("17", "viktig.PDF", 100L, "en/url/"),
            Vedleggreferanse("101", "filUtenSuffix", 100L, "en/url/"),
            Vedleggreferanse("5", "katt.Png", 100L, "en/url/"),
            Vedleggreferanse("71", "endaEn.pdf", 100L, "en/url/"),
            Vedleggreferanse("22", "ugyldigSuffix.doc", 100L, "en/url/"),
            Vedleggreferanse("56", "mere.pdF", 100L, "en/url/"),
        )

        val antallPerType: List<VedleggtypeOgAntall> = VedleggHelper.tellAntallVedleggPerType(vedleggreferanser)

        assertThat(antallPerType.size).isEqualTo(4)
        assertThat(antallPerType.map { it.vedleggtype }).containsSequence(
            Vedleggtype.PDF,
            Vedleggtype.JPEG,
            Vedleggtype.PNG,
            Vedleggtype.ANNET
        )

        assertThat(antallPerType[0]).isEqualTo(VedleggtypeOgAntall(Vedleggtype.PDF, 3))
        assertThat(antallPerType[1]).isEqualTo(VedleggtypeOgAntall(Vedleggtype.JPEG, 2))
        assertThat(antallPerType[2]).isEqualTo(VedleggtypeOgAntall(Vedleggtype.PNG, 1))
        assertThat(antallPerType[3]).isEqualTo(VedleggtypeOgAntall(Vedleggtype.ANNET, 2))
    }

}