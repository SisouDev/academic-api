package com.institution.management.academic_api.application.dto.academic;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar ou atualizar um Departamento.")
public record DepartmentRequestDto(
        @Schema(description = "Nome completo do departamento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Departamento de Engenharia de Software")
        String name,

        @Schema(description = "Sigla do departamento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "DESW")
        String acronym,

        @Schema(description = "ID da instituição à qual o departamento pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long institutionId
) {}