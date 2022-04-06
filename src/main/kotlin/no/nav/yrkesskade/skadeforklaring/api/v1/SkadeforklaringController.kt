package no.nav.yrkesskade.skadeforklaring.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.yrkesskade.skadeforklaring.config.CorrelationInterceptor
import no.nav.yrkesskade.skadeforklaring.integration.mottak.model.Spraak
import no.nav.yrkesskade.skadeforklaring.model.Skadeforklaring
import no.nav.yrkesskade.skadeforklaring.security.AutentisertBruker
import no.nav.yrkesskade.skadeforklaring.security.ISSUER
import no.nav.yrkesskade.skadeforklaring.security.LEVEL
import no.nav.yrkesskade.skadeforklaring.services.SkadeforklaringService
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ProtectedWithClaims(issuer = ISSUER, claimMap = [LEVEL])
@Tag(name = "Skadeforklaring API", description = "Skadeforklaring API")
@RestController
@RequestMapping(
    path = ["/v1/skadeforklaringer"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE])
class SkadeforklaringController(private val autentisertBruker: AutentisertBruker, private val skadeforklaringService: SkadeforklaringService) {

    @Operation(summary = "Send inn skadeforklaring")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "201", description = "Skadeforklaring opprettet",
            content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal Server Error", content = [Content()]),
    ]
    )
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postSkadeforklaring(@RequestBody skadeforklaring: Skadeforklaring): ResponseEntity<Void> {
        skadeforklaringService.sendMelding(skadeforklaring, Spraak.NB)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}