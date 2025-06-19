package com.institution.management.academic_api.application.dto.institution;

import com.institution.management.academic_api.application.dto.common.AddressDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Representação detalhada de uma Instituição.")
public record InstitutionDetailsDto(
        @Schema(description = "ID único da instituição.", example = "1")
        Long id,

        @Schema(description = "Nome oficial da instituição.", example = "Universidade Federal de Spring")
        String name,

        @Schema(description = "CNPJ ou outro ID de registro.", example = "12.345.678/0001-99")
        String registerId,

        @Schema(description = "Data de cadastro da instituição.")
        LocalDateTime createdAt,

        AddressDto address,

        @Schema(description = "Lista de departamentos da instituição.")
        List<DepartmentSummaryDto> departments,

        @Schema(description = "Total de membros (alunos, professores, etc.) na instituição.", example = "4582")
        Integer membersCount,

        @Schema(description = "Lista dos administradores principais da instituição.")
        List<PersonSummaryDto> admins
) {}


