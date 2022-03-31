package no.nav.yrkesskade.skadeforklaring.storage

import no.nav.yrkesskade.skadeforklaring.storage.providers.GoogleStore
import no.nav.yrkesskade.skadeforklaring.storage.providers.MemoryStore

object StorageProvider {

    fun getStorage(type: StorageType): Store = when (type) {
        StorageType.MEMORY -> MemoryStore.getInstance()
        StorageType.GCP -> GoogleStore.getInstance()
    }
}

enum class StorageType {
    MEMORY,
    GCP
}