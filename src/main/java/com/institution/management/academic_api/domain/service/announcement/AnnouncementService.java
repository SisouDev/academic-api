package com.institution.management.academic_api.domain.service.announcement;

import com.institution.management.academic_api.application.dto.announcement.AnnouncementDetailsDto;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;
import com.institution.management.academic_api.application.dto.announcement.CreateAnnouncementRequestDto;
import com.institution.management.academic_api.application.dto.announcement.UpdateAnnouncementRequestDto;

import java.util.List;

public interface AnnouncementService {
    AnnouncementDetailsDto create(CreateAnnouncementRequestDto dto, String creatorEmail);

    AnnouncementDetailsDto update(Long id, UpdateAnnouncementRequestDto dto);

    void delete(Long id);

    AnnouncementDetailsDto findById(Long id);

    List<AnnouncementSummaryDto> findVisibleForCurrentUser();

    List<AnnouncementSummaryDto> findByCourseSection(Long courseSectionId);

}
