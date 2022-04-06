package no.nav.yrkesskade.skadeforklaring.integration.mottak

import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.SkadeforklaringInnsendingHendelse
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture

@Component
class SkadeforklaringInnsendingClient(
    @Value("\${kafka.topic.skadeforklaring-innsendt}") private val topic: String,
    private val skadeforklaringKafkaTemplate: KafkaTemplate<String, SkadeforklaringInnsendingHendelse>
) {

    fun sendMelding(skadeforklaringInnsendingHendelse: SkadeforklaringInnsendingHendelse): SkadeforklaringInnsendingHendelse {
        val future: ListenableFuture<SendResult<String, SkadeforklaringInnsendingHendelse>> = skadeforklaringKafkaTemplate.send(topic, skadeforklaringInnsendingHendelse);
        val resultat = future.get();

        return resultat.producerRecord.value();
    }
}