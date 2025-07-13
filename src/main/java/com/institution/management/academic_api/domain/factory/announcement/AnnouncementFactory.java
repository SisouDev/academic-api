package com.institution.management.academic_api.domain.factory.announcement;

import com.institution.management.academic_api.application.dto.announcement.CreateAnnouncementRequestDto;
import com.institution.management.academic_api.application.mapper.simple.announcement.AnnouncementMapper;
import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AnnouncementFactory {

    private final AnnouncementMapper announcementMapper;
    private final DepartmentRepository departmentRepository;

    public Announcement create(CreateAnnouncementRequestDto dto, Person creator) {
        Announcement announcement = announcementMapper.toEntity(dto);

        announcement.setCreatedBy(creator);
        announcement.setCreatedAt(LocalDateTime.now());

        AnnouncementScope scope = AnnouncementScope.valueOf(dto.scope());
        announcement.setScope(scope);

        return announcement;
    }
}