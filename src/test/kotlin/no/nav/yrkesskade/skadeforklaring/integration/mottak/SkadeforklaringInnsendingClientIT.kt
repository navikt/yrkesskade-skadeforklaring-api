package no.nav.yrkesskade.skadeforklaring.integration.mottak

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringInnsendingHendelse
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringMetadata
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.Spraak
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.test.AbstractTest
import no.nav.yrkesskade.skadeforklaring.test.fixtures.getEnkelskadeforklaring
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest
class SkadeforklaringInnsendingClientIT : AbstractTest() {

    @Autowired
    private lateinit var skadeforklaringInnsendingClient: SkadeforklaringInnsendingClient

    private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `send melding til mottak`() {
        val skadeforklaring: Skadeforklaring = getEnkelskadeforklaring()
        val skadeforklaringInnsendingHendelse = SkadeforklaringInnsendingHendelse(
            skadeforklaring = skadeforklaring,
            metadata = SkadeforklaringMetadata(
                tidspunktMottatt = Instant.now(),
                spraak = Spraak.NB,
                navCallId = UUID.randomUUID().toString()
            ),
        )
        assertThat(skadeforklaringInnsendingHendelse.skadeforklaring).isNotNull()
        assertThat(skadeforklaringInnsendingHendelse.metadata.spraak).isEqualTo(Spraak.NB)
        skadeforklaringInnsendingClient.sendMelding(skadeforklaringInnsendingHendelse)
        mottakConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS)
        assertThat(mottakConsumer.getPayload()).contains("\"norskIdentitetsnummer\":\"12345678910\"")
    }
}