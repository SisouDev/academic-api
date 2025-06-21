package com.institution.management.academic_api.domain.repository.institution;

import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    Optional<Institution> findByRegisterId(String registerId);

    Optional<Institution> findByNameIgnoreCase(String name);

    Page<Institution> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
