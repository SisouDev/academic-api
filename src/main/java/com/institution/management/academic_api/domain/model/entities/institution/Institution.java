package com.institution.management.academic_api.domain.model.entities.institution;

import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Address;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institutions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String registerId;

    @Embedded
    private Address address;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Department> departments = new ArrayList<>();

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<AcademicTerm> academicTerms = new ArrayList<>();

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Person> members = new ArrayList<>();

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<InstitutionAdmin> admins = new ArrayList<>();
}
