package com.institution.management.academic_api.application.dto.employee;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados para cadastrar um novo funcionário.")
public record CreateEmployeeRequestDto(
        @Schema(description = "Primeiro nome.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Roberto")
        String firstName,

        @Schema(description = "Sobrenome.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Gomes")
        String lastName,

        @Schema(description = "Email.", requiredMode = Schema.RequiredMode.REQUIRED, example = "roberto.gomes@example.com")
        String email,

        @Schema(description = "Cargo do funcionário.", requiredMode = Schema.RequiredMode.REQUIRED, example = "COORDINATOR")
        String jobPosition,

        @Schema(description = "Data de contratação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-11-10")
        LocalDate hiringDate,

        @Schema(description = "ID da instituição.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long institutionId,

        DocumentDto document
) {}