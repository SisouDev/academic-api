package com.institution.management.academic_api.application.dto.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(
        description = "Representação base de uma pessoa no sistema.",
        discriminatorProperty = "personType",
        oneOf = {StudentResponseDto.class, TeacherResponseDto.class, EmployeeResponseDto.class, InstitutionAdminResponseDto.class},
        discriminatorMapping = {
                @DiscriminatorMapping(value = "STUDENT", schema = StudentResponseDto.class),
                @DiscriminatorMapping(value = "TEACHER", schema = TeacherResponseDto.class),
                @DiscriminatorMapping(value = "EMPLOYEE", schema = EmployeeResponseDto.class),
                @DiscriminatorMapping(value = "STAFF", schema = InstitutionAdminResponseDto.class)
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "personType", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StudentResponseDto.class, name = "STUDENT"),
        @JsonSubTypes.Type(value = TeacherResponseDto.class, name = "TEACHER"),
        @JsonSubTypes.Type(value = EmployeeResponseDto.class, name = "EMPLOYEE"),
        @JsonSubTypes.Type(value = InstitutionAdminResponseDto.class, name = "STAFF")
})

@Getter
@Setter
public abstract class PersonResponseDto {

    @Schema(description = "ID único da pessoa.", example = "101")
    private Long id;

    @Schema(description = "Primeiro nome.", example = "Ana")
    private String firstName;

    @Schema(description = "Sobrenome.", example = "Souza")
    private String lastName;

    @Schema(description = "Email de contato.", example = "ana.souza@example.com")
    private String email;

    @Schema(description = "Status da pessoa no sistema.", example = "ACTIVE")
    private String status;

    @Schema(description = "Data e hora do cadastro.")
    private LocalDateTime createdAt;

    private DocumentDto document;

    private InstitutionSummaryDto institution;

    public abstract String getPersonType();
}