package no.nav.yrkesskade.skadeforklaring.test

import no.nav.yrkesskade.kodeverk.mockserver.AbstractMockSever
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
@ConditionalOnProperty(
    value = arrayOf("mock.enabled"),
    havingValue = "true",
    matchIfMissing = false
)
class TestMockServer() : AbstractMockSever(null)
