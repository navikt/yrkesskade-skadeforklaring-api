package no.nav.yrkesskade.skadeforklaring.test.fixtures

import no.nav.yrkesskade.skadeforklaring.model.*
import java.time.Instant

fun getEnkelskadeforklaring() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", rolle = "Foresatt"),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    vedleggtype = "Papir",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "sykemelding"),
    behandler = Behandler(behandlerNavn = "Test Testesen", erBehandlerOppsokt = true, adresse = null)
)

fun getEnkelskadeforklaringUgyldigFravaertype() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", rolle = null),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    vedleggtype = "Papir",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "denne finnes ikke"),
    behandler = Behandler(behandlerNavn = "Test Testesen", erBehandlerOppsokt = true, adresse = null)
)

fun getEnkelskadeforklaringMedFeilPostnummer() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", rolle = null),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    vedleggtype = "Papir",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "Sykemelding"),
    behandler = Behandler(behandlerNavn = "Test Testesen", erBehandlerOppsokt = true, adresse = Adresse(adresse = "Ringveien 2", postnummer = "TEST", poststed = "Test"))
)