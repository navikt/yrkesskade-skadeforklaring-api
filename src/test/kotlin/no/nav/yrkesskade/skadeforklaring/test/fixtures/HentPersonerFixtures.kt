package no.nav.yrkesskade.skadeforklaring.test.fixtures

import com.expediagroup.graphql.client.jackson.types.JacksonGraphQLResponse
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersoner
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.*

fun okResponsPersonerFraPdl(): GraphQLClientResponse<HentPersoner.Result> {
    return JacksonGraphQLResponse(
        data = HentPersoner.Result(gyldigePersonerMedNavnOgFoedsel()),
        errors = null,
        extensions = emptyMap()
    )
}

fun gyldigePersonerMedNavnOgFoedsel(): List<HentPersonBolkResult> {
    return listOf(
        HentPersonBolkResult("0123456789", gyldigPersonMedNavnOgFoedsel("Erik Olsen", 2000, "01-01-2000", null)),
        HentPersonBolkResult("2123449435", gyldigPersonMedNavnOgFoedsel("Hege Olsen", 2010, "01-01-2010", null))
    )
}

fun gyldigPersonMedNavnOgFoedsel(forkortetnavn: String, foedselsaar: Int, foedselsdato: String, doedsdato: String?) = Person(
    listOf(Navn(forkortetnavn)),
    listOf(Doedsfall(doedsdato)),
    listOf(Foedsel(foedselsaar, foedselsdato))
)