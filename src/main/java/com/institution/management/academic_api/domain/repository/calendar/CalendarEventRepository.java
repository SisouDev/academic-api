package com.institution.management.academic_api.domain.repository.calendar;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    @Query("SELECT e FROM CalendarEvent e WHERE (e.scope = 'INSTITUTION' OR e.targetDepartment.id = :departmentId) AND e.startTime < :end AND e.endTime > :start")
    List<CalendarEvent> findVisibleEventsForPeriod(
            @Param("departmentId") Long departmentId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT ce FROM CalendarEvent ce WHERE " +
            "(ce.scope = :scope OR ce.targetDepartment = :department) AND " +
            "ce.startTime > :currentTime " +
            "ORDER BY ce.startTime ASC")
    List<CalendarEvent> findUpcomingEvents(
            @Param("scope") AnnouncementScope scope,
            @Param("department") Department department,
            @Param("currentTime") LocalDateTime currentTime,
            Pageable pageable);
}
