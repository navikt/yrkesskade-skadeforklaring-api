package no.nav.yrkesskade.skadeforklaring.test.fixtures

import com.expediagroup.graphql.client.jackson.types.JacksonGraphQLResponse
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersoner
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.Foedsel
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.HentPersonBolkResult
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.Navn
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersoner.Person

fun okResponsPersonerFraPdl(): GraphQLClientResponse<HentPersoner.Result> {
    return JacksonGraphQLResponse(
        data = HentPersoner.Result(gyldigePersonerMedNavnOgFoedsel()),
        errors = null,
        extensions = emptyMap()
    )
}

fun gyldigePersonerMedNavnOgFoedsel(): List<HentPersonBolkResult> {
    return listOf(
        HentPersonBolkResult("0123456789", gyldigPersonMedNavnOgFoedsel("Erik Olsen", 2000, "01-01-2000")),
        HentPersonBolkResult("2123449435", gyldigPersonMedNavnOgFoedsel("Hege Olsen", 2010, "01-01-2010"))
    )
}

fun gyldigPersonMedNavnOgFoedsel(forkortetnavn: String, foedselsaar: Int, foedselsdato: String) = Person(
    listOf(Navn(forkortetnavn)),
    listOf(Foedsel(foedselsaar, foedselsdato))
)