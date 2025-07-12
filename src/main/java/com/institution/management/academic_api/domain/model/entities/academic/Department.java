package com.institution.management.academic_api.domain.model.entities.academic;

import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String acronym;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    @ToString.Exclude
    private Institution institution;

    @OneToMany(mappedBy = "targetDepartment", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InternalRequest> internalRequests = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "targetDepartment", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Announcement> announcements = new ArrayList<>();
}
