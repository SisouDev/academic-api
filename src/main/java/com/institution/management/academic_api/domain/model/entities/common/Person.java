package com.institution.management.academic_api.domain.model.entities.common;

import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
    private String phone;
    @Enumerated(EnumType.STRING)
    private PersonStatus status;
    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institution_id", nullable = false)
    @ToString.Exclude
    private Institution institution;

    public abstract PersonType getPersonType();

}
