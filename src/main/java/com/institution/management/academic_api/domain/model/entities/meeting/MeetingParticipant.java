package com.institution.management.academic_api.domain.model.entities.meeting;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.meeting.MeetingParticipantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meeting_participants")
@Getter
@Setter
public class MeetingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person participant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingParticipantStatus status;

    public MeetingParticipant() {
        this.status = MeetingParticipantStatus.PENDING;
    }
}