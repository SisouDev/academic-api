package com.institution.management.academic_api.domain.repository.meeting;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.model.entities.meeting.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    Optional<MeetingParticipant> findByMeetingAndParticipant(Meeting meeting, Person userPerson);
}
