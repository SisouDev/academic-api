package com.institution.management.academic_api.application.dto.employee;

import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "Representação DETALHADA de uma pessoa do tipo Funcionário (Employee).")
@Getter
@Setter
public class EmployeeDetailsDto extends PersonResponseDto {

    @Schema(description = "Cargo do funcionário.", example = "SECRETARY")
    private String jobPosition;

    @Schema(description = "Data de contratação.", example = "2022-03-15")
    private LocalDate hiringDate;

    @Schema(description = "Departamento ao qual o funcionário pertence.")
    private DepartmentSummaryDto department;

    @Schema(description = "Anos de serviço do funcionário na instituição.", example = "2")
    private long yearsOfService;

    @Override
    public String getPersonType() {
        return "EMPLOYEE";
    }
}