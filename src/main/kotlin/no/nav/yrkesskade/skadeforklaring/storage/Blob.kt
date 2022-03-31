package no.nav.yrkesskade.skadeforklaring.storage

data class Blob(val id: String, val bruker: String, val bytes: ByteArray?, val navn: String?, val storrelse: Long?) {
    override fun toString(): String {
        return "Blob($id, $bruker, $navn, $storrelse)"
    }
}