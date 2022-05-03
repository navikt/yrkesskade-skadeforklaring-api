package no.nav.yrkesskade.skadeforklaring.services

import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor
import no.nav.yrkesskade.skadeforklaring.integration.mottak.SkadeforklaringInnsendingClient
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringInnsendingHendelse
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringMetadata
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.Spraak
import no.nav.yrkesskade.skadeforklaring.metric.MetricService
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.utils.getSecureLogger
import org.slf4j.MDC
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SkadeforklaringService(
    private val skadeforklaringInnsendingClient: SkadeforklaringInnsendingClient,
    private val metricService: MetricService
) {

    private val secureLog = getSecureLogger()

    fun sendMelding(
        skadeforklaring: Skadeforklaring,
        spraak: Spraak
    ): SkadeforklaringInnsendingHendelse {
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
}