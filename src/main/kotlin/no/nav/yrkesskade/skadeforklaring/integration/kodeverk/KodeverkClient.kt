package no.nav.yrkesskade.skadeforklaring.integration.kodeverk

import no.nav.yrkesskade.kodeverk.model.KodeverdiDto
import no.nav.yrkesskade.kodeverk.model.KodeverdiResponsDto
import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor
import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor.Companion.CORRELATION_ID_HEADER_NAME
import org.checkerframework.checker.units.qual.K
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.netty.http.client.HttpClient

@Component
class KodeverkClient(@Value("\${integration.client.kodeverk.url}") val kodeverkUrl: String) {

    private val client: WebClient

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    init {
        client = WebClient.builder()
            .baseUrl(kodeverkUrl)
            .clientConnector(ReactorClientHttpConnector(HttpClient.newConnection()))
            .build()
    }

    @Cacheable
    @Retryable
    fun hentKodeverk(type: String, spraak: String = "nb"): Map<String, KodeverdiDto>? {
        log.info(
            "Kaller ys-kodeverk - type=$type, spraak=$spraak"
        )
        return logTimingAndWebClientResponseException("hentLand") {
            kallKodeverkApi(type)
        }
    }

    private fun kallKodeverkApi(type: String): Map<String, KodeverdiDto>? {
        val kodeverdiRespons = client.get()
            .uri { uriBuilder ->
                uriBuilder.pathSegment(
                    "api",
                    "v1",
                    "kodeverk",
                    "typer",
                    type,
                    "kodeverdier"
                )
                    .build()
            }
            .header(
                CORRELATION_ID_HEADER_NAME, MDC.get(
                    CorrelationInterceptor.CORRELATION_ID_LOG_VAR_NAME
                )
            )
            .retrieve()
            .bodyToMono<KodeverdiResponsDto>()
            .block() ?: KodeverdiResponsDto(emptyMap())
        // TODO: YSMOD-161 - Send med callid i header for enklere feilsøking

        return kodeverdiRespons.kodeverdierMap
    }

    @Suppress("SameParameterValue")
    private fun <T> logTimingAndWebClientResponseException(methodName: String, function: () -> T): T {
        val start: Long = System.currentTimeMillis()
        try {
            return function.invoke()
        } catch (ex: WebClientResponseException) {
            log.error(
                "Got a {} error calling kodeverk {} {} with message {}",
                ex.statusCode,
                ex.request?.method ?: "-",
                ex.request?.uri ?: "-",
                ex.responseBodyAsString
            )
            throw ex
        } catch (rtex: RuntimeException) {
            log.error("Caught RuntimeException while calling kodeverk ", rtex)
            throw rtex
        } finally {
            val end: Long = System.currentTimeMillis()
            log.info("Method {} took {} millis", methodName, (end - start))
        }
    }
}