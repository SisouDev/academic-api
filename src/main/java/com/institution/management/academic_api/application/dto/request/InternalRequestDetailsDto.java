package com.institution.management.academic_api.application.dto.request;

import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de uma requisição interna.")
public record InternalRequestDetailsDto(
        @Schema(description = "ID da requisição.", example = "101")
        Long id,

        @Schema(description = "Título da requisição.", example = "Projetor da sala 301 não liga")
        String title,

        @Schema(description = "Descrição completa.")
        String description,

        @Schema(description = "Tipo da requisição.", example = "MAINTENANCE_REQUEST")
        String type,

        @Schema(description = "Status atual.", example = "PENDING")
        String status,

        @Schema(description = "Nível de urgência.", example = "HIGH")
        String urgency,

        @Schema(description = "Data e hora de criação.")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora da resolução.")
        LocalDateTime resolvedAt,

        @Schema(description = "Notas sobre a resolução.")
        String resolutionNotes,

        @Schema(description = "Funcionário que abriu a requisição.")
        PersonSummaryDto requester,

        @Schema(description = "Departamento alvo da requisição.")
        DepartmentSummaryDto targetDepartment,

        @Schema(description = "Funcionário que atendeu (ou está atendendo) a requisição.")
        PersonSummaryDto handler
) {}