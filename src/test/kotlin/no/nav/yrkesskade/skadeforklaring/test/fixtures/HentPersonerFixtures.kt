package no.nav.yrkesskade.skadeforklaring.test.fixtures

import com.expediagroup.graphql.client.jackson.types.JacksonGraphQLResponse
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.HentPerson
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentperson.Doedsfall
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentperson.Foedsel
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentperson.Navn
import no.nav.yrkesskade.skadeforklaring.integration.pdl.graphql.generated.hentperson.Person
import kotlin.random.Random

fun okResponsPersonerFraPdl(): GraphQLClientResponse<HentPerson.Result> {
    return JacksonGraphQLResponse(
        data = HentPerson.Result(gyldigePersonerMedNavnOgFoedsel()),
        errors = null,
        extensions = emptyMap()
    )
}

fun gyldigePersonerMedNavnOgFoedsel(): Person {
    return listOf(
        gyldigPersonMedNavnOgFoedsel("Erik Olsen", 2000, "01-01-2000", null),
        gyldigPersonMedNavnOgFoedsel("Hege Olsen", 2010, "01-01-2010", null),
        gyldigPersonMedNavnOgFoedsel("Sanne Olsen", 2000, "01-01-2000", null),
        gyldigPersonMedNavnOgFoedsel("Cedrik Olsen", 2010, "01-01-2010", null),
        gyldigPersonMedNavnOgFoedsel("Olaf Olsen", 2000, "01-01-2000", null),
        gyldigPersonMedNavnOgFoedsel("Edda Olsen", 2010, "01-01-2010", null),
        gyldigPersonMedNavnOgFoedsel("Åge Olsen", 2000, "01-01-2000", null),
        gyldigPersonMedNavnOgFoedsel("Svein Olsen", 2010, "01-01-2010", null),
        gyldigPersonMedNavnOgFoedsel("Harald Olsen", 2000, "01-01-2000", null),
        gyldigPersonMedNavnOgFoedsel("Ester Olsen", 2010, "01-01-2010", null),
    )[Random.nextInt(0, 9)]

}

fun gyldigPersonMedNavnOgFoedsel(forkortetnavn: String, foedselsaar: Int, foedselsdato: String, doedsdato: String?) = Person(
    listOf(Navn(forkortetnavn)),
    listOf(Foedsel(foedselsaar, foedselsdato)),
    listOf(Doedsfall(doedsdato)),
)