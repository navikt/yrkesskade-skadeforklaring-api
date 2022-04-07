package no.nav.yrkesskade.skadeforklaring.storage

data class Blob(val id: String, val bruker: String, val bytes: ByteArray?, val navn: String?, val storrelse: Long?) {
    override fun toString(): String {
        // toString som fjerner bytes
        return "Blob(id='$id', bruker='$bruker', navn=$navn, storrelse=$storrelse)"
    }
}