package no.nav.yrkesskade.skadeforklaring.services

import no.bekk.bekkopen.person.FodselsnummerValidator
import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor
import no.nav.yrkesskade.skadeforklaring.integration.mottak.SkadeforklaringInnsendingClient
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringInnsendingHendelse
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringMetadata
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.Spraak
import no.nav.yrkesskade.skadeforklaring.metric.MetricService
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.utils.KodeverkValidator
import no.nav.yrkesskade.skadeforklaring.utils.getSecureLogger
import org.slf4j.MDC
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SkadeforklaringService(
    private val skadeforklaringInnsendingClient: SkadeforklaringInnsendingClient,
    private val metricService: MetricService,
    private val kodeverkValidator: KodeverkValidator
) {

    private val secureLog = getSecureLogger()

    fun validerOgSendMelding(
        skadeforklaring: Skadeforklaring,
        spraak: Spraak
    ): SkadeforklaringInnsendingHendelse {
        validerSkadeforklaring(skadeforklaring, spraak)

        val skadeforklaringInnsendingHendelse = SkadeforklaringInnsendingHendelse(
            metadata = SkadeforklaringMetadata(
                tidspunktMottatt = Instant.now(), spraak = spraak, navCallId = MDC.get(
                    CorrelationInterceptor.CORRELATION_ID_LOG_VAR_NAME
                )
            ),
            skadeforklaring = skadeforklaring
        )
        return skadeforklaringInnsendingClient.sendMelding(skadeforklaringInnsendingHendelse).also {
            secureLog.info("Sendt skadeforklaring $it til mottak")
            metricService.insertMetrikk(skadeforklaringInnsendingHendelse)
        }
    }

    /**
     * Validering av skadeforklaring utfører diverse sjekker og valideringer
     *
     * Kaster exceptions dersom en validering feiler
     */
    private fun validerSkadeforklaring(skadeforklaring: Skadeforklaring, spraak: Spraak) {
        check(skadeforklaring.helseinstitusjon.erHelsepersonellOppsokt == "nei" || skadeforklaring.helseinstitusjon.erHelsepersonellOppsokt == "ja", { "${skadeforklaring.helseinstitusjon.erHelsepersonellOppsokt} er ikke en gyldig verdi erHelsepersonellOppsokt. Kan være 'ja' eller 'nei'" })
        check(skadeforklaring.skalEttersendeDokumentasjon == "ja" || skadeforklaring.skalEttersendeDokumentasjon == "nei", { "${skadeforklaring.skalEttersendeDokumentasjon} er ikke en gyldig verdi skalEttersendeDokumentasjon. Kan være 'ja' eller 'nei'"})

        val innmelderNorskIdentitetsnummer = skadeforklaring.innmelder.norskIdentitetsnummer
        check(!innmelderNorskIdentitetsnummer.isNullOrEmpty(), {"Innmelders fødselsnummer kan ikke være null eller tom"})
        check(
            FodselsnummerValidator.isValid(innmelderNorskIdentitetsnummer),
            { "Innmelders fødselsnummer er ugyldig. '$innmelderNorskIdentitetsnummer' er ikke gyldig norsk person identitetsnummer" })

        val paavegneAvNorskIdentitetsnummer = skadeforklaring.skadelidt.norskIdentitetsnummer
        check(!paavegneAvNorskIdentitetsnummer.isNullOrEmpty(), {"Skadelidts fødselsnummer kan ikke være null eller tom"})
        check(
            FodselsnummerValidator.isValid(paavegneAvNorskIdentitetsnummer),
            { "Skadelidts fødselsnummer er ugyldig. '$paavegneAvNorskIdentitetsnummer' er ikke gyldig norsk person identitetsnummer" })


        if (skadeforklaring.helseinstitusjon.erHelsepersonellOppsokt == "ja") {
            check(skadeforklaring.helseinstitusjon.adresse != null, { "Adresse er påkrevd når erHelsepersonellOppsokt verdi er 'ja'"})
        }
        if (!skadeforklaring.helseinstitusjon.adresse?.postnummer.isNullOrBlank()) {
            check(skadeforklaring.helseinstitusjon.adresse?.postnummer?.toIntOrNull() != null,
                { "Postnummer kan kun bestå av siffer" })
        }

        // valider fravaer
        kodeverkValidator.sjekkGyldigKodeverkverdi(skadeforklaring.fravaer.foerteDinSkadeEllerSykdomTilFravaer, "foerteDinSkadeEllerSykdomTilFravaer", "${skadeforklaring.fravaer.foerteDinSkadeEllerSykdomTilFravaer} er ikke en gyldig kode. Sjekk kodeverk 'foerteDinSkadeEllerSykdomTilFravaer' for gyldige koder")

        // sjekk fravaertype dersom skadelidt har hatt fravaer
        val harIkkeHattFravaer = listOf("nei", "ikkeRelevant")
        if (!harIkkeHattFravaer.contains(skadeforklaring.fravaer.foerteDinSkadeEllerSykdomTilFravaer)) {
            kodeverkValidator.sjekkGyldigKodeverkverdi(
                skadeforklaring.fravaer.fravaertype!!,
                "fravaertype",
                "${skadeforklaring.fravaer.fravaertype} er ikke en gyldig kode. Sjekk 'fravaertype' for gyldige koder"
            )
        }
    }
}