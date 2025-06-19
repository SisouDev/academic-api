package com.institution.management.academic_api.application.dto.teacher;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "Representação detalhada de uma pessoa do tipo Professor.")
@Getter
@Setter
public class TeacherResponseDto extends PersonResponseDto {

    @Schema(description = "Nível de formação acadêmica do professor.", example = "MASTER")
    private String academicBackground;

    @Schema(description = "Lista de turmas (resumidas) que este professor está lecionando.")
    private List<CourseSectionSummaryDto> courseSections;

    @Override
    public String getPersonType() {
        return "TEACHER";
    }
}

