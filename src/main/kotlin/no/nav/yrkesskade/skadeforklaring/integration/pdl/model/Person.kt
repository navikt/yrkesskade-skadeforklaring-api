package no.nav.yrkesskade.skadeforklaring.integration.pdl.model

data class Person(val identifikator: String, val navn: String, val foedselsaar: Int, val foedselsdato: String, val doedsdato: String?, val foreldreansvar: List<Person>?)