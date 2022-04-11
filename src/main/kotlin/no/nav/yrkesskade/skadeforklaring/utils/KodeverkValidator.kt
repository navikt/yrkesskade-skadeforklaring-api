package no.nav.yrkesskade.skadeforklaring.utils

import no.nav.yrkesskade.skadeforklaring.integration.kodeverk.KodeverkClient
import org.springframework.stereotype.Component

@Component
class KodeverkValidator(val kodeverkClient: KodeverkClient) {

    fun sjekkGyldigKodeverkverdi(verdi: String, kodeverktypenavn: String, feilmelding: String) {
        check(kodeverkClient.hentKodeverk(kodeverktypenavn).orEmpty().containsKey(verdi), { feilmelding })
    }

}