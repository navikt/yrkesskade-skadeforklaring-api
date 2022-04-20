package no.nav.yrkesskade.skadeforklaring.vedlegg

class AttachmentVirusException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}