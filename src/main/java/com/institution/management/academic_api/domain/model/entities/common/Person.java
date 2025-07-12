package com.institution.management.academic_api.domain.model.entities.common;

import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "people")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;
    @Column
    private String lastName;

    @Embedded
    private Document document;

    @Column
    private String email;

    @Column
    private String profilePictureUrl;

    @Column
    private String phone;

    @Enumerated(EnumType.STRING)
    private PersonStatus status;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institution_id", nullable = false)
    @ToString.Exclude
    private Institution institution;

    @OneToMany(
            mappedBy = "requester",
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private List<InternalRequest> createdRequests = new ArrayList<>();

    @OneToMany(mappedBy = "handler", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InternalRequest> handledRequests = new ArrayList<>();

    @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Loan> loans = new ArrayList<>();

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Reservation> reservations = new ArrayList<>();

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;


    public abstract PersonType getPersonType();

}
