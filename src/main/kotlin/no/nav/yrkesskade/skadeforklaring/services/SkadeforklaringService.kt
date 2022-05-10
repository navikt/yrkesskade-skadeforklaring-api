package no.nav.yrkesskade.skadeforklaring.services

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

    fun sendMelding(
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