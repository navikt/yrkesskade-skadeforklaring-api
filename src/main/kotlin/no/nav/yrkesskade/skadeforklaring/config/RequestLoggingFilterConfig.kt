package no.nav.yrkesskade.skadeforklaring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class RequestLoggingFilterConfig {
    @Bean
    fun logFilter(): CommonsRequestLoggingFilter =
        CommonsRequestLoggingFilter().apply {
            setIncludePayload(true)
            setMaxPayloadLength(5000)
            setIncludeHeaders(true)
            setIncludeQueryString(true)
        }
}