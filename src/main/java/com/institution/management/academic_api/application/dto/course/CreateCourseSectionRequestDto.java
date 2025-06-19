package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar uma nova Turma (Course Section).")
public record CreateCourseSectionRequestDto(
        @Schema(description = "Nome/código da turma.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Turma A - Noturno")
        String name,

        @Schema(description = "Sala ou local da turma.", example = "Sala 203B")
        String room,

        @Schema(description = "ID da Disciplina ofertada.", requiredMode = Schema.RequiredMode.REQUIRED, example = "55")
        Long subjectId,

        @Schema(description = "ID do Período Letivo em que ocorre.", requiredMode = Schema.RequiredMode.REQUIRED, example = "42")
        Long academicTermId,

        @Schema(description = "ID do Professor responsável.", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
        Long teacherId
) {}