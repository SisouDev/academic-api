package com.institution.management.academic_api.application.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar o status ou o total de faltas de uma Matrícula.")
public record UpdateEnrollmentRequestDto(
        @Schema(description = "Novo status da matrícula.", example = "DROPPED")
        String status,

        @Schema(description = "Novo total de faltas acumuladas. Use com cuidado, pode ser um campo calculado.", example = "5")
        Integer totalAbsences
) {}