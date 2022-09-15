package no.nav.yrkesskade.skadeforklaring.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.Size

data class Helseinstitusjon(
    @Size(max=150)
    val navn: String?,
)