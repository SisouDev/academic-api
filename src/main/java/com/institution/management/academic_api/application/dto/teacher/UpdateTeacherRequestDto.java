package com.institution.management.academic_api.application.dto.teacher;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente um Professor. Apenas os campos fornecidos serão alterados.")
public record UpdateTeacherRequestDto(
        @Schema(description = "Novo email de contato.", example = "beatriz.new@example.com")
        String email,

        @Schema(description = "Novo telefone de contato.", example = "+55 (31) 99999-8888")
        String phone,

        @Schema(description = "Novo status do professor no sistema.", example = "INACTIVE")
        String status,

        @Schema(description = "Nova formação acadêmica.", example = "POSTDOC")
        String academicBackground
) {}