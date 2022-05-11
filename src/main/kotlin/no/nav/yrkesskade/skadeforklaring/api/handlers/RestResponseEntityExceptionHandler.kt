package no.nav.yrkesskade.skadeforklaring.api.handlers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import no.nav.yrkesskade.skadeforklaring.utils.getLogger
import no.nav.yrkesskade.skadeforklaring.vedlegg.AttachmentVirusException
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.invoke.MethodHandles
import java.time.Instant

@RestControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    val log = getLogger(MethodHandles.lookup().lookupClass())

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
        ]
    )
    fun handleIllegalStateException(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any> = handleExceptionAndLogError(ex, request, HttpHeaders(), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(AttachmentVirusException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "Virus oppdaget i vedlegg",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
        ]
    )
    fun handleAttachmentVirusFound(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any> = handleExceptionAndLogError(ex, request, HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY)

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
    ): ResponseEntity<Any> = handleExceptionAndLogError(ex, request, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)

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
    ): ResponseEntity<Any> = handleExceptionAndLogError(ex, request, HttpHeaders(), HttpStatus.PAYLOAD_TOO_LARGE)

    private fun handleExceptionAndLogError(ex: Exception, request: WebRequest, headers: HttpHeaders, httpStatus: HttpStatus): ResponseEntity<Any> {
        log.error("${ex.javaClass.simpleName}: ${ex.message} \n${ex.stackTraceToString()}")
        return handleExceptionInternal(ex, ErrorResponse.fraException(ex, httpStatus.value()), headers, httpStatus, request)
    }
}