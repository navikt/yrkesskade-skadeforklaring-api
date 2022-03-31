package no.nav.yrkesskade.skadeforklaring.config

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootConfiguration
@ConfigurationPropertiesScan
@EnableJwtTokenValidation(
    ignore = ["org.springframework", "org.springdoc"]
)
class ApplicationConfig {
}