package no.nav.yrkesskade.skadeforklaring.test.fixtures

import no.nav.yrkesskade.skadeforklaring.model.*
import java.time.Instant

fun getEnkelskadeforklaring() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = "Foresatt"),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "sykemelding"),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "nei", adresse = null)
)

fun getEnkelskadeforklaringMedHelsepersonellOppsoekt() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = "Foresatt"),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "sykemelding"),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "ja", adresse = Adresse(adresse = "Testveien 1", postnummer = "1000", poststed = "Teststed"))
)

fun getEnkelskadeforklaringMedHelsepersonellOppsoektUtenAdresse() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = "Foresatt"),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "sykemelding"),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "ja", adresse = null)
)

fun getEnkelskadeforklaringUgyldigFravaertype() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = null),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "denne finnes ikke"),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "nei", adresse = null)
)

fun getEnkelskadeforklaringIngenFravaer() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = null),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "nei", fravaertype = ""),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "nei", adresse = null)
)

fun getEnkelskadeforklaringOenskerIkkeOppgiFravaer() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = null),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "oenskerIkkeAaOppgi", fravaertype = ""),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "nei", adresse = null)
)

fun getEnkelskadeforklaringMedFeilPostnummer() = Skadeforklaring(
    saksnummer = null,
    innmelder = Innmelder(norskIdentitetsnummer = "12345678910", innmelderrolle = null),
    skadelidt = Skadelidt(norskIdentitetsnummer = "123456798810"),
    arbeidetMedIUlykkesoeyeblikket = "En kort arbeidsbeskrivelse",
    noeyaktigBeskrivelseAvHendelsen = "En litt lengre ulykkesbeskrivelse",
    tid = Tid(tidspunkt = Instant.now(), tidstype = Tidstype.TIDSPUNKT, periode = null),
    skalEttersendeDokumentasjon = "ja",
    vedleggreferanser = emptyList(),
    fravaer = Fravaer(foerteDinSkadeEllerSykdomTilFravaer = "treDagerEllerMindre", fravaertype = "Sykemelding"),
    helseinstitusjon = Helseinstitusjon(navn = "Test Testesen", erHelsepersonellOppsokt = "ja", adresse = Adresse(adresse = "Ringveien 2", postnummer = "TEST", poststed = "Test"))
)