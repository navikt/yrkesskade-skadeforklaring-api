package no.nav.yrkesskade.skadeforklaring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue

const val DEFAULT_CLAM_URL = "http://clamav.clamav.svc.cluster.local/scan"

@ConfigurationProperties(prefix = "virusscan")
@ConstructorBinding
class VirusScannerConfig(@DefaultValue(DEFAULT_CLAM_URL) val url: String, @DefaultValue("true") val enabled: Boolean) {
}