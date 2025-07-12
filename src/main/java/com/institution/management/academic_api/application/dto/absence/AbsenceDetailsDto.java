package com.institution.management.academic_api.application.dto.absence;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de um registro de ausência.")
public record AbsenceDetailsDto(
        @Schema(description = "ID do registro.", example = "1")
        Long id,

        @Schema(description = "Tipo da ocorrência.", example = "Falta")
        String type,

        @Schema(description = "Data da ocorrência.", example = "2025-10-20")
        LocalDate date,

        @Schema(description = "Justificativa enviada pelo funcionário.")
        String justification,

        @Schema(description = "URL do anexo (se houver).", example = "https://storage.cloud.com/attachments/medical_cert_123.pdf")
        String attachmentUrl,

        @Schema(description = "Status da justificativa.", example = "Aprovada")
        String status,

        @Schema(description = "Data do registro no sistema.")
        LocalDateTime createdAt,

        @Schema(description = "Data da análise pelo gestor.")
        LocalDateTime reviewedAt,

        @Schema(description = "Funcionário relacionado à ausência.")
        PersonSummaryDto person,

        @Schema(description = "Gestor que analisou a justificativa.")
        PersonSummaryDto reviewedBy
) {}