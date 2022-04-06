package no.nav.yrkesskade.skadeforklaring.test.fixtures

import no.nav.yrkesskade.skadeforklaring.model.Behandler
import no.nav.yrkesskade.skadeforklaring.model.Fravaer
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.model.Tid
import java.time.Instant

fun getEnkelskadeforklaring() = Skadeforklaring(
    identifikator = "12345678910",
    "En kort arbeidsbeskrivelse",
    "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = "Tidspunkt", periode = null),
    vedleggtype = "Papir",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(harFravaer = true, fravaertype = "Sykemelding"),
    behandler = Behandler(behandlerNavn = "Test Testesen", erBehandlerOppsokt = true)
)