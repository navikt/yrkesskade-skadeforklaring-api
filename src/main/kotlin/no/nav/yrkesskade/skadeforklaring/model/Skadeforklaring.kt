package no.nav.yrkesskade.skadeforklaring.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.Size

data class Skadeforklaring(
    val saksnummer: String?,
    val innmelder: Innmelder,
    val skadelidt: Skadelidt,
    @Size(max = 1000)
    val arbeidetMedIUlykkesoeyeblikket: String,
    @Size(max = 2000)
    val noeyaktigBeskrivelseAvHendelsen: String,
    val tid: Tid,
    @Schema(example = "ferdig", description = "Informasjon om dokumentasjon skal ettersendes eller om alt er lagt ved. Gyldige verdier er 'ja','nei' og 'ferdig'")
    val skalEttersendeDokumentasjon: String,
    val vedleggreferanser: List<Vedleggreferanse>,
    val fravaer: Fravaer,
    @Schema(example = "ja", description = "Beskriver om skadelidt har vært hos medisinsk behandler. Gyldige verdier er 'ja' eller 'nei'")
    val erHelsepersonellOppsokt: String,
    val helseinstitusjoner: List<Helseinstitusjon>,
    @Schema(example = "2022-02-02", description = "Dato for når første helsepersonell ble oppsøkt")
    val foersteHelsepersonellOppsoktDato: LocalDate?
)