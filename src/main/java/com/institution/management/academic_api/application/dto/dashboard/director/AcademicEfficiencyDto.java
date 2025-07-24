package com.institution.management.academic_api.application.dto.dashboard.director;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Contém indicadores de eficiência acadêmica.")
public record AcademicEfficiencyDto(
        @Schema(description = "Média geral das notas de todos os alunos.", example = "8.2")
        BigDecimal averageStudentGrade,

        @Schema(description = "Taxa média de frequência dos alunos.", example = "92.5")
        double averageAttendanceRate,

        @Schema(description = "Taxa de aprovação geral.", example = "88.0")
        double overallApprovalRate
) {}