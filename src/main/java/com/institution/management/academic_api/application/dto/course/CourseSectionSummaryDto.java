package com.institution.management.academic_api.application.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de uma Turma.")
public record CourseSectionSummaryDto(Long id, String name, SubjectCourseInfo subjectInfo) {}