package com.institution.management.academic_api.application.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Representação de um registro de presença.")
public record AttendanceRecordDto(
        Long id,
        LocalDate date,
        Boolean wasPresent
) {}