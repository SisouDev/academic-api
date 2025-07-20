package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados para criar um novo Curso.")
public record CreateCourseRequestDto(
        @Schema(description = "Nome do curso.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Engenharia de Computação")
        String name,

        @Schema(description = "Descrição detalhada do curso.", example = "Curso focado em hardware e software...")
        String description,

        @Schema(description = "Duração do curso em semestres.", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
        Integer durationInSemesters,

        @Schema(description = "ID do Departamento ao qual o curso pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
        Long departmentId,

        BigDecimal tuitionFee
) {}