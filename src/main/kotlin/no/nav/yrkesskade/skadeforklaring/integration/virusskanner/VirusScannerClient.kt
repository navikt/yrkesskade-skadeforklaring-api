package no.nav.yrkesskade.skadeforklaring.integration.virusskanner

import no.nav.yrkesskade.skadeforklaring.config.VirusScannerConfig
import no.nav.yrkesskade.skadeforklaring.integration.virusskanner.model.Result
import no.nav.yrkesskade.skadeforklaring.integration.virusskanner.model.ScanResult
import no.nav.yrkesskade.skadeforklaring.utils.getLogger
import no.nav.yrkesskade.skadeforklaring.vedlegg.AttachmentVirusException
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

@Component
class VirusScannerClient(val virusScannerConfig: VirusScannerConfig) {

    private val client: WebClient

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = getLogger(javaClass.enclosingClass)
    }

    init {
        client = WebClient.builder()
            .baseUrl(virusScannerConfig.toString())
            .clientConnector(ReactorClientHttpConnector(HttpClient.newConnection()))
            .build()
    }

    fun isEnabled(): Boolean = virusScannerConfig.enabled

    fun scan(data: ByteArray, name: String) {
        if (!isEnabled()) {
            logger.warn("Virusskanner er deaktivert")
            return
        }

        if (data.isEmpty()) {
            logger.info("Virusscanning ikke utført. Ingen data å skanne")
            return
        }

        val scanResults = client.put().uri("").body(Mono.just(data), ByteArray::class.java).retrieve().bodyToMono<Array<ScanResult>>().block().orEmpty()

        if (scanResults.size !== 1) {
            logger.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.size)
            throw AttachmentVirusException(name)
        }

        val scanResult = scanResults.first()
        logger.info("Fikk scan result $scanResult")

        if (scanResult.result === Result.OK) {
            logger.trace("Ingen virus i $scanResult")
            return
        }

        logger.warn("Virus funnet i ${scanResult.filename}! Status ${scanResult.result}")
        throw AttachmentVirusException(name)

    }
}