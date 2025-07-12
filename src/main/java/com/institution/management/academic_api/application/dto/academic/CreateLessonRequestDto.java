package com.institution.management.academic_api.application.dto.academic;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateLessonRequestDto(
        @NotNull(message = "O ID da turma é obrigatório.")
        Long courseSectionId,

        @NotBlank(message = "O tópico da aula é obrigatório.")
        String topic,

        String description,

        @NotNull(message = "A data da aula é obrigatória.")
        @FutureOrPresent(message = "A data da aula não pode ser no passado.")
        LocalDate lessonDate
) {}