package com.institution.management.academic_api.application.dto.student;
import com.institution.management.academic_api.application.dto.common.AddressDto;
import com.institution.management.academic_api.application.dto.common.DocumentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para cadastrar um novo aluno.")
public record CreateStudentRequestDto(
        @Schema(description = "Primeiro nome.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Carlos")
        String firstName,

        @Schema(description = "Sobrenome.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Mendes")
        String lastName,

        @Schema(description = "Email.", requiredMode = Schema.RequiredMode.REQUIRED, example = "carlos.mendes@example.com")
        String email,

        @Schema(description = "Data de nascimento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2006-01-15")
        LocalDate birthDate,

        @Schema(description = "ID da instituição onde o aluno será matriculado.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long institutionId,

        DocumentDto document,
        AddressDto address
) {}