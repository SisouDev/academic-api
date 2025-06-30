package com.institution.management.academic_api.application.dto.employee;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados para cadastrar um novo funcionário.")
public record CreateEmployeeRequestDto(
        @Schema(description = "Primeiro nome.", requiredMode = Schema.RequiredMode.REQUIRED)
        String firstName,

        @Schema(description = "Sobrenome.", requiredMode = Schema.RequiredMode.REQUIRED)
        String lastName,

        @Schema(description = "Email.", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "Cargo do funcionário.", requiredMode = Schema.RequiredMode.REQUIRED)
        String jobPosition,

        @Schema(description = "Data de contratação.", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate hiringDate,

        @Schema(description = "ID da instituição.", requiredMode = Schema.RequiredMode.REQUIRED)
        Long institutionId,

        @Schema(description = "ID do departamento ao qual o funcionário pertence.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long departmentId,

        DocumentDto document
) {}
