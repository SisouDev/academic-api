package com.institution.management.academic_api.application.dto.helpDesk;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar um chamado de suporte (geralmente por um técnico).")
public record UpdateSupportTicketRequestDto(
        @Schema(description = "Novo status do chamado.", example = "IN_PROGRESS")
        String status,

        @Schema(description = "Nova prioridade do chamado.", example = "HIGH")
        String priority,

        @Schema(description = "ID do funcionário (técnico) ao qual o chamado está sendo atribuído.", example = "31")
        Long assigneeId
) {}