package com.institution.management.academic_api.domain.repository.teacher;

import com.institution.management.academic_api.domain.model.entities.common.StaffMember;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    boolean existsByEmail(String email);

    List<Teacher> findAllByInstitution(Institution institution);

    Optional<Teacher> findByEmail(String username);

    long countByStatus(PersonStatus status);

    Optional<Teacher> findByUser_Login(String username);

    List<? extends StaffMember> findAllByStatus(PersonStatus personStatus);
}
