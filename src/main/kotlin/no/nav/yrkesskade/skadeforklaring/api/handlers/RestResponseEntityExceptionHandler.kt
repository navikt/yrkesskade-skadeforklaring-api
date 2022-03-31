package no.nav.yrkesskade.skadeforklaring.api.handlers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

@RestControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(
        value = [
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
    fun handleAllExceptions(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any?>? {
        val body = ErrorResponse(
            melding = ex.message!!,
            statuskode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            tidspunkt = Instant.now()
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }

    @ExceptionHandler(FileSizeLimitExceededException::class, MaxUploadSizeExceededException::class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "413",
                description = "Payload too large",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
        ]
    )
    fun handleFileSizeLimitExceededException(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any?>? {
        val body = ErrorResponse(
            melding = ex.message!!,
            statuskode = HttpStatus.PAYLOAD_TOO_LARGE.value(),
            tidspunkt = Instant.now()
        )

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(body)
    }
}