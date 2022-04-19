package no.nav.yrkesskade.skadeforklaring.vedlegg

import no.nav.yrkesskade.skadeforklaring.integration.virusskanner.VirusScannerClient
import no.nav.yrkesskade.skadeforklaring.model.Vedlegg
import org.springframework.stereotype.Service

@Service
class Virusskanner(val virusScannerClient: VirusScannerClient) {

    fun sjekk(vararg vedlegg: Vedlegg) {
        vedlegg.forEach {
            virusScannerClient.scan(it.data, it.navn)
        }
    }
}