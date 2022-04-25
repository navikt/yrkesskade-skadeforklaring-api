package no.nav.yrkesskade.skadeforklaring.test

import no.nav.yrkesskade.skadeforklaring.mockserver.AbstractMockServer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(
    value = arrayOf("service.wiremock.enabled"),
    havingValue = "true",
    matchIfMissing = false
)
class TestMockServer() : AbstractMockServer(null)
