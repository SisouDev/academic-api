package com.institution.management.academic_api.domain.factory.student;
import com.institution.management.academic_api.application.dto.student.CreateAttendanceRecordRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.AttendanceRecordMapper;
import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AttendanceRecordFactory {

    private final AttendanceRecordMapper attendanceRecordMapper;

    public AttendanceRecord create(CreateAttendanceRecordRequestDto requestDto) {
        AttendanceRecord record = attendanceRecordMapper.toEntity(requestDto);
        record.setDate(LocalDate.now());
        return record;
    }
}