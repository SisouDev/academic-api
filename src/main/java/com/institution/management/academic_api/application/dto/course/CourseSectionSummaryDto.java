package com.institution.management.academic_api.application.dto.course;

import com.institution.management.academic_api.application.dto.teacher.TeacherSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de uma Turma.")
public record CourseSectionSummaryDto(Long id, String name, TeacherSummaryDto teacher, SubjectCourseInfo subjectInfo) {}