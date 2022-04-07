package no.nav.yrkesskade.skadeforklaring.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.yrkesskade.skadeforklaring.model.Brukerinfo
import no.nav.yrkesskade.skadeforklaring.security.AutentisertBruker
import no.nav.yrkesskade.skadeforklaring.security.ISSUER
import no.nav.yrkesskade.skadeforklaring.security.LEVEL
import no.nav.yrkesskade.skadeforklaring.services.BrukerService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@ProtectedWithClaims(issuer = ISSUER, claimMap = [LEVEL])
@Tag(name = "Brukerinfo API", description = "Brukerinformasjon API")
@RestController
@RequestMapping(
    path = ["/v1/brukerinfo"], produces = [MediaType.APPLICATION_JSON_VALUE]
)
class BrukerinfoController(val autentisertBruker: AutentisertBruker, private val brukerService: BrukerService) {

    @Operation(summary = "Hent informasjon om pålogget bruker")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Brukerinfo", content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = Brukerinfo::class)))]
            ),
            ApiResponse(responseCode = "500", description = "Internal Server Error", content = [Content()]),
        ]
    )
    @GetMapping
    fun hentBrukerinfo(): ResponseEntity<Brukerinfo> {
        val brukerinfo = brukerService.hentBrukerinfo(autentisertBruker.fodselsnummer)

        return ResponseEntity.ok(brukerinfo)
    }
}