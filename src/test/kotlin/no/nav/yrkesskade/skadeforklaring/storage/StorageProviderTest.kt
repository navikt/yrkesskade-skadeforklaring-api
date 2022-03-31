package no.nav.yrkesskade.skadeforklaring.storage

import no.nav.yrkesskade.skadeforklaring.storage.providers.MemoryStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StorageProviderTest {

    @Test
    fun `storage provider henter en store`() {
        val store = StorageProvider.getStorage(StorageType.MEMORY)
        assertThat(store.javaClass).isEqualTo(MemoryStore.javaClass)
    }
}