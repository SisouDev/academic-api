package com.institution.management.academic_api.application.dto.academic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLessonContentRequestDto(
        @NotNull(message = "O ID da aula é obrigatório.")
        Long lessonId,

        @NotBlank(message = "O tipo de conteúdo é obrigatório.")
        String type,

        @NotBlank(message = "O conteúdo não pode estar vazio.")
        String content,

        int displayOrder
) {}