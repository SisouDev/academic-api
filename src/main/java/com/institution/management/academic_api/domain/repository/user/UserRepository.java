package com.institution.management.academic_api.domain.repository.user;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT u FROM User u
        JOIN FETCH u.person p
        LEFT JOIN FETCH Teacher t ON t.id = p.id
        LEFT JOIN FETCH Student s ON s.id = p.id
        LEFT JOIN FETCH Employee e ON e.id = p.id
        LEFT JOIN FETCH InstitutionAdmin a ON a.id = p.id
        WHERE u.login = :login
    """)
    Optional<User> findByLoginWithFullPerson(@Param("login") String login);

    @Query("""
    SELECT u FROM User u
    JOIN FETCH u.person p
    LEFT JOIN FETCH u.roles
    WHERE u.id = :id
""")
    Optional<User> findByIdWithPerson(@Param("id") Long id);




}
