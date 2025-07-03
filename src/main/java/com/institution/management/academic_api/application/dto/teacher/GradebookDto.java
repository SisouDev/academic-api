package com.institution.management.academic_api.application.dto.teacher;

import java.util.List;

public record GradebookDto(
        List<GradebookHeaderDto> headers,
        List<GradebookStudentRowDto> studentRows
) {}
