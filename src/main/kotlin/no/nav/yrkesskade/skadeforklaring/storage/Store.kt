package no.nav.yrkesskade.skadeforklaring.storage

interface Store {
    fun putBlob(blob: Blob): String
    fun getBlob(blob: Blob): Blob?
    fun deleteBlob(blob: Blob): Boolean
}