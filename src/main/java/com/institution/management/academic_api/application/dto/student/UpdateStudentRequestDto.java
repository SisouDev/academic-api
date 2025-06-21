package com.institution.management.academic_api.application.dto.student;

import com.institution.management.academic_api.application.dto.common.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados para atualizar parcialmente um Aluno. Apenas os campos fornecidos serão alterados.")
public record UpdateStudentRequestDto(
        @Schema(description = "Novo email de contato.", example = "carlos.mendes.new@example.com")
        String email,

        @Schema(description = "Novo telefone de contato.", example = "+55 (11) 98765-4321")
        String phone,

        @Schema(description = "Novo status do aluno no sistema.", example = "INACTIVE")
        String status,

        @Schema(description = "Nova data de nascimento.", example = "2006-01-16")
        LocalDate birthDate,

        @Schema(description = "Novo endereço completo do aluno.")
        AddressDto address,
        String firstName,
        String lastName
) {}