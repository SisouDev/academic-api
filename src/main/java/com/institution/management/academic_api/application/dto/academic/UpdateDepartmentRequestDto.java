package com.institution.management.academic_api.application.dto.academic;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente um Departamento. Apenas os campos fornecidos serão alterados.")
public record UpdateDepartmentRequestDto(
        @Schema(description = "Novo nome completo do departamento.", example = "Departamento de Ciência da Computação")
        String name,

        @Schema(description = "Nova sigla do departamento.", example = "DCC")
        String acronym
) {}