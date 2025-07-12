package com.institution.management.academic_api.domain.repository.meeting;

import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m JOIN m.participants p WHERE (m.organizer.id = :personId OR p.id = :personId) AND m.startTime < :end AND m.endTime > :start")
    List<Meeting> findMeetingsByPersonAndPeriod(
            @Param("personId") Long personId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
