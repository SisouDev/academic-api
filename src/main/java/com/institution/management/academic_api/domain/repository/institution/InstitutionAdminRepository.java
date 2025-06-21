package com.institution.management.academic_api.domain.repository.institution;

import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionAdminRepository extends JpaRepository<InstitutionAdmin, Long> {
    boolean existsInstitutionAdminByEmail(String email);
}
