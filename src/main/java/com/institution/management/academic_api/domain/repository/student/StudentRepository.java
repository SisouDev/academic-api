package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
