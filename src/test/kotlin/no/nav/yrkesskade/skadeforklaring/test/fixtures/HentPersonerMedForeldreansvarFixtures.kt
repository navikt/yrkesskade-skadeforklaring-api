package no.nav.yrkesskade.skadeforklaring.test.fixtures

import com.expediagroup.graphql.client.jackson.types.JacksonGraphQLResponse
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPersonMedForeldreansvar
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentpersonmedforeldreansvar.*

fun okResponsPersonMedForeldreansvarFraPdl(): GraphQLClientResponse<HentPersonMedForeldreansvar.Result> {
    return JacksonGraphQLResponse(
        data = HentPersonMedForeldreansvar.Result(gyldigPersonMedForeldreansvar()),
        errors = null,
        extensions = emptyMap()
    )
}

fun gyldigPersonMedForeldreansvar(): Person {
    return Person(
        listOf(Navn("Ola Normann")),
        listOf(Foedsel(2000, "01-01-2000")),
        listOf(Foreldreansvar("01234567890")),
        listOf(ForelderBarnRelasjon("0123456789"))
    )
}


