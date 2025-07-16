package com.institution.management.academic_api.domain.repository.absence;

import com.institution.management.academic_api.domain.model.entities.absence.Absence;
import com.institution.management.academic_api.domain.model.enums.absence.AbsenceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByPersonId(Long personId);
    List<Absence> findByStatus(AbsenceStatus status);
    Page<Absence> findAllByStatus(AbsenceStatus status, Pageable pageable);

}
