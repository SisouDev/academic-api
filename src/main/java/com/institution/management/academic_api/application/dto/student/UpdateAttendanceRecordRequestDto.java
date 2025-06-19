package com.institution.management.academic_api.application.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para corrigir um registro de presença.")
public record UpdateAttendanceRecordRequestDto(
        @Schema(description = "Correção da data da aula.", example = "2025-09-04")
        LocalDate date,

        @Schema(description = "Correção do status de presença.", example = "false")
        Boolean wasPresent
) {}