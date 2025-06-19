package com.institution.management.academic_api.application.dto.employee;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "Representação detalhada de uma pessoa do tipo Funcionário (Employee).")
@Getter
@Setter
public class EmployeeResponseDto extends PersonResponseDto {

    @Schema(description = "Cargo do funcionário.", example = "SECRETARY")
    private String jobPosition;

    @Schema(description = "Data de contratação.", example = "2022-03-15")
    private LocalDate hiringDate;

    @Override
    public String getPersonType() {
        return "EMPLOYEE";
    }
}