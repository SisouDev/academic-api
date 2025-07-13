package com.institution.management.academic_api.domain.repository.announcement;

import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Query("SELECT a FROM Announcement a WHERE a.scope = 'INSTITUTION' OR a.targetDepartment.id = :departmentId")
    List<Announcement> findVisibleAnnouncements(Long departmentId);

    List<Announcement> findByScope(AnnouncementScope scope);

    List<Announcement> findTop3ByOrderByCreatedAtDesc();

    List<Announcement> findByTargetCourseSectionIdOrderByCreatedAtDesc(Long courseSectionId);
}
