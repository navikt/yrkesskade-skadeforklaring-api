package no.nav.yrkesskade.skadeforklaring.services

import no.nav.yrkesskade.skadeforklaring.model.Vedlegg
import no.nav.yrkesskade.skadeforklaring.vedlegg.Virusskanner
import no.nav.yrkesskade.storage.Blob
import no.nav.yrkesskade.storage.StorageProvider
import no.nav.yrkesskade.storage.StorageType
import no.nav.yrkesskade.storage.Store

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StorageService(@Value("\${storage.type:MEMORY}") val storageType: String, val virusskanner: Virusskanner) {

    val storage: Store

    init {
        storage = StorageProvider.getStorage(StorageType.valueOf(storageType))
    }

    fun lastopp(id: String, filnavn: String, bytes: ByteArray, storrelse: Long, brukerIdentifikator: String): String {
        virusskanner.sjekk(Vedlegg(bytes, id))

        return storage.putBlob(
            Blob(
                id = id,
                bruker = brukerIdentifikator,
                bytes = bytes,
                navn = filnavn,
                storrelse = storrelse
            )
        )
    }

    fun slett(id: String, brukerIdentifikator: String): Boolean =
        storage.deleteBlob(
            Blob(
                id = id,
                bruker = brukerIdentifikator,
                null,
                null,
                null
            )
        )

}