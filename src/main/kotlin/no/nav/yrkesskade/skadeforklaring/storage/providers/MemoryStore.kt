package no.nav.yrkesskade.skadeforklaring.storage.providers

import no.nav.yrkesskade.skadeforklaring.storage.Blob
import no.nav.yrkesskade.skadeforklaring.storage.Store

object MemoryStore : Store {

    private val storage: HashMap<String, Blob>

    init {
        storage = HashMap()
    }

    fun getInstance(): Store = this;

    override fun putBlob(blob: Blob): String {
        storage.put(blob.id, blob)

        return "/blob/${blob.id}"
    }

    override fun getBlob(blob: Blob): Blob? {
        return storage.get(blob.id)

    }

    override fun deleteBlob(blob: Blob): Boolean {
        storage.remove(blob.id)
        return true
    }
}