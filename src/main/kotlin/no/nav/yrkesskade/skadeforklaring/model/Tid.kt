package no.nav.yrkesskade.skadeforklaring.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import javax.validation.constraints.NotBlank

@Schema(
    name = "Tid",
    description = "Tid for ulykken. Kan være et tidspunkt eller over en periode"
)
data class Tid(
    @NotBlank
    @Schema(type = "string", allowableValues = ["Tidspunkt", "Periode"])
    val tidstype: String,

    val tidspunkt: Instant?,
    val periode: Periode?
)

@Schema(
    name = "Periode",
    description = "Angir fra og til datoer"
)
data class Periode(
    @NotBlank
    val fra: Instant,
    @NotBlank
    val til: Instant,
)