package com.institution.management.academic_api.domain.repository.common;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Optional<Person> findByEmail(String email);

    List<Person> findAllByStatus(PersonStatus status);

    List<Person> findAllByInstitution(Institution institution);

    Page<Person> findAllByInstitution(Institution institution, Pageable pageable);

    Page<Person> findByInstitutionAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            Institution institution, String firstName, String lastName, Pageable pageable);

    Optional<Person> findByDocument_Number(String documentNumber);

    @Query("SELECT p FROM Person p WHERE p.institution.id = :institutionId AND TYPE(p) IN (Employee, InstitutionAdmin)")
    List<Person> findAllStaffByInstitutionId(@Param("institutionId") Long institutionId);

    boolean existsByEmail(String mail);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.profilePictureUrl = :url WHERE p.id = :id")
    void updateProfilePictureUrl(@Param("id") Long id, @Param("url") String url);
}
