package com.institution.management.academic_api.domain.repository.user;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    List<User> findAllByActive(boolean active);

    Optional<User> findByPerson(Person person);

    Page<User> findByRoles_Name(RoleName roleName, Pageable pageable);
    List<User> findByRoles_Name(RoleName roleName);

    Page<User> findByPerson_Institution_Id(Long institutionId, Pageable pageable);

    Optional<User> findByPersonId(Long personId);

    boolean existsByLogin(String login);

    List<User> findByRoles_NameIn(List<RoleName> roleLibrarian);
}
