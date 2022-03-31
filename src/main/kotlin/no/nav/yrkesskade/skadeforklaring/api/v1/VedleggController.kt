package no.nav.yrkesskade.skadeforklaring.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.yrkesskade.skadeforklaring.api.handlers.ErrorResponse
import no.nav.yrkesskade.skadeforklaring.security.AutentisertBruker
import no.nav.yrkesskade.skadeforklaring.security.ISSUER
import no.nav.yrkesskade.skadeforklaring.security.LEVEL
import no.nav.yrkesskade.skadeforklaring.services.StorageService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@ProtectedWithClaims(issuer = ISSUER, claimMap = [LEVEL])
@Tag(name = "Vedlegg API", description = "API for opplast av vedlegg til en skadeforklaring")
@RestController
@RequestMapping(path = ["/v1/vedlegg"])
class VedleggController(val autentisertBruker: AutentisertBruker, val storageService: StorageService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Last opp vedlegg. Max 8MB")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Vedlegg lastet opp",
                content = [Content()],
                headers = [Header(name = "location")]
            ),
        ]
    )
    fun lastOppVedlegg(
        @RequestPart("id") id: String,
        @RequestParam("vedlegg") vedlegg: MultipartFile
    ): ResponseEntity<Void> {
        val url = storageService.lastopp(
            id,
            vedlegg.originalFilename.orEmpty(),
            vedlegg.bytes,
            vedlegg.size,
            autentisertBruker.fodselsnummer
        )
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, url).build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Slett vedlegg")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Vedlegg slettet",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
        ]
    )
    fun slettVedlegg(@PathVariable("id") id: String): ResponseEntity<Void> {
        val slettet = storageService.slett(id, autentisertBruker.fodselsnummer)
        if (!slettet) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}