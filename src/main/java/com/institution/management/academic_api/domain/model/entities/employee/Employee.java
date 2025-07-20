package com.institution.management.academic_api.domain.model.entities.employee;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.common.StaffMember;
import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import com.institution.management.academic_api.domain.model.entities.it.Asset;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Employee extends StaffMember {

    @Enumerated(EnumType.STRING)
    private JobPosition jobPosition;

    @Column
    private LocalDate hiringDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department department;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Task> createdTasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Task> assignedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Announcement> createdAnnouncements = new ArrayList<>();

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Asset> assignedAssets = new ArrayList<>();

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<LeaveRequest> createdLeaveRequests = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<LeaveRequest> reviewedLeaveRequests = new ArrayList<>();

    @Override
    public PersonType getPersonType() {
        return PersonType.EMPLOYEE;
    }
}
