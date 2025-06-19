package com.institution.management.academic_api.application.dto.common;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa um documento de identificação.")
public record DocumentDto(
        @Schema(description = "Tipo do documento.", example = "NATIONAL_ID")
        String type,

        @Schema(description = "Número do documento.", example = "12.345.678-9")
        String number
) {}