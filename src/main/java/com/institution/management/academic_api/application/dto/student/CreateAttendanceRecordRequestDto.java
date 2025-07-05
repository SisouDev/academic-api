package com.institution.management.academic_api.application.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registrar uma nova presença/falta para uma matrícula.")
public record CreateAttendanceRecordRequestDto(
        @Schema(description = "ID da matrícula.", requiredMode = Schema.RequiredMode.REQUIRED, example = "250")
        Long enrollmentId,

        @Schema(description = "Indica se o aluno esteve presente.", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        Boolean wasPresent
) {}