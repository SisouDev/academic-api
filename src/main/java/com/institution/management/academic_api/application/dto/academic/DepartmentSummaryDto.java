package com.institution.management.academic_api.application.dto.academic;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de um Departamento.")
public record DepartmentSummaryDto(Long id, String name, String acronym) {}