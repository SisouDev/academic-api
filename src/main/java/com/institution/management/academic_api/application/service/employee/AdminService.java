package com.institution.management.academic_api.application.service.employee;

import com.institution.management.academic_api.application.dto.common.ActivityLogDto;
import com.institution.management.academic_api.application.dto.course.CourseStudentCountDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.common.ActivityLogMapper;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionMapper;
import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.common.ActivityLogRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final CourseRepository courseRepository;
    private final ActivityLogRepository activityLogRepository;
    private final ActivityLogMapper activityLogMapper;

    @Transactional(readOnly = true)
    public InstitutionDetailsDto getDashboardStatistics() {
        Long MAIN_INSTITUTION_ID = 1L;
        Institution institution = institutionRepository.findById(MAIN_INSTITUTION_ID)
                .orElseThrow(() -> new RuntimeException("Institution not found!"));

        return institutionMapper.toDetailsDto(institution);
    }

    public List<CourseStudentCountDto> getStudentDistribution() {
        return courseRepository.getStudentCountPerCourse();
    }

    @Transactional(readOnly = true)
    public List<ActivityLogDto> getRecentActivityLogs() {
        List<ActivityLog> recentLogs = activityLogRepository.findTop5ByOrderByTimestampDesc();
        return recentLogs.stream()
                .map(activityLogMapper::toDto)
                .collect(Collectors.toList());
    }
}