package com.institution.management.academic_api.application.dto.student;
import com.institution.management.academic_api.application.dto.common.AddressDto;
import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import java.util.List;

@Schema(description = "Representação detalhada de uma pessoa do tipo Aluno.")
@Getter
@Setter
public class StudentResponseDto extends PersonResponseDto {

    @Schema(description = "Data de nascimento do aluno.", example = "2005-10-20")
    private LocalDate birthDate;

    private AddressDto address;

    @Schema(description = "Lista de matrículas (resumidas) do aluno.")
    private List<EnrollmentSummaryDto> enrollments;

    @Schema(description = "Média geral do aluno em todas as disciplinas.", example = "8.5")
    private double generalAverage;

    @Schema(description = "Total de faltas acumuladas do aluno em todas as disciplinas.", example = "12")
    private int totalAbsences;

    @Override
    public String getPersonType() {
        return "STUDENT";
    }
}