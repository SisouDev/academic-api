package com.institution.management.academic_api.domain.repository.academic;

import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicTermRepository extends JpaRepository<AcademicTerm, Long> {
    Optional<AcademicTerm> findByYearAndSemesterAndInstitution(Year year, Integer semester, Institution institution);

    List<AcademicTerm> findByInstitutionAndStatus(Institution institution, AcademicTermStatus status);

    Optional<AcademicTerm> findByInstitutionAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Institution institution, LocalDate date, LocalDate sameDate);

    boolean existsByYearAndSemesterAndInstitution(Year year, Integer semester, Institution institution);

    List<AcademicTerm> findAllByInstitutionId(Long institutionId);
}
