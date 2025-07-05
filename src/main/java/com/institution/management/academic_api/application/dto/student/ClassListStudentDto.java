package com.institution.management.academic_api.application.dto.student;

import com.institution.management.academic_api.domain.model.enums.student.DailyAttendanceStatus;

public record ClassListStudentDto(
        Long enrollmentId,
        Long studentId,
        String studentName,
        String studentEmail,
        String status,
        DailyAttendanceStatus todaysAttendance
) {}