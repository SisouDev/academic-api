package com.institution.management.academic_api.domain.factory.announcement;

import com.institution.management.academic_api.application.dto.announcement.CreateAnnouncementRequestDto;
import com.institution.management.academic_api.application.mapper.simple.announcement.AnnouncementMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AnnouncementFactory {

    private final AnnouncementMapper announcementMapper;
    private final DepartmentRepository departmentRepository;

    public Announcement create(CreateAnnouncementRequestDto dto, Employee creator) {
        Announcement announcement = announcementMapper.toEntity(dto);

        announcement.setCreatedBy(creator);
        announcement.setCreatedAt(LocalDateTime.now());

        AnnouncementScope scope = AnnouncementScope.valueOf(dto.scope());
        announcement.setScope(scope);

        if (scope == AnnouncementScope.DEPARTMENT) {
            if (dto.targetDepartmentId() == null) {
                throw new IllegalArgumentException("targetDepartment Id is required for department scoped notices.");
            }
            Department targetDepartment = departmentRepository.findById(dto.targetDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.targetDepartmentId()));
            announcement.setTargetDepartment(targetDepartment);
        } else {
            announcement.setTargetDepartment(null);
        }

        return announcement;
    }
}