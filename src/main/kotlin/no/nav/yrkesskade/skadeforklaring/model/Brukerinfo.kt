package no.nav.yrkesskade.skadeforklaring.model

data class Brukerinfo(val identifikator: String, val navn: String, val fodselsdato: String, val juridiskAnsvarFor: List<Person>)