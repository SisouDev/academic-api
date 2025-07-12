package com.institution.management.academic_api.application.dto.calendar;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de um evento do calendário.")
public record CalendarEventDetailsDto(
        @Schema(description = "ID único do evento.", example = "1")
        Long id,

        @Schema(description = "Título do evento.")
        String title,

        @Schema(description = "Descrição detalhada do evento.")
        String description,

        @Schema(description = "Tipo do evento.", example = "Feriado")
        String type,

        @Schema(description = "Data e hora de início.")
        LocalDateTime startTime,

        @Schema(description = "Data e hora de término.")
        LocalDateTime endTime,

        @Schema(description = "Escopo de visibilidade.", example = "Institucional")
        String scopeDisplay,

        @Schema(description = "Departamento alvo (se aplicável).")
        DepartmentSummaryDto targetDepartment
) {}