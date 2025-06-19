package com.institution.management.academic_api.application.dto.institution;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Representação detalhada de uma pessoa do tipo Administrador da Instituição (Staff).")
@Getter
@Setter
public class InstitutionAdminResponseDto extends PersonResponseDto {
    @Override
    public String getPersonType() {
        return "STAFF";
    }
}