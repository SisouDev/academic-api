package com.institution.management.academic_api.application.dto.student;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Representação detalhada de uma Matrícula.")
public record EnrollmentDetailsDto(
        @Schema(description = "ID da matrícula.", example = "250")
        Long id,

        @Schema(description = "Data da matrícula.", example = "2025-07-25")
        LocalDate enrollmentDate,

        @Schema(description = "Status da matrícula.", example = "ACTIVE")
        String status,

        @Schema(description = "Total de faltas acumuladas.", example = "4")
        Integer totalAbsences,

        @Schema(description = "Informações resumidas do aluno desta matrícula.")
        PersonSummaryDto student,

        @Schema(description = "Informações da turma desta matrícula.")
        CourseSectionSummaryDto courseSection,

        @Schema(description = "Histórico de avaliações da matrícula.")
        List<AssessmentDto> assessments,

        @Schema(description = "Histórico de frequência da matrícula.")
        List<AttendanceRecordDto> attendanceRecords
) {}