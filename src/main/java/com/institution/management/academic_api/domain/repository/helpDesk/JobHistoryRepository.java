package com.institution.management.academic_api.domain.repository.helpDesk;

import com.institution.management.academic_api.domain.model.entities.employee.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {

    @Query("SELECT jh FROM JobHistory jh WHERE jh.person.id = :personId AND jh.endDate IS NULL")
    Optional<JobHistory> findActiveByPersonId(Long personId);

    List<JobHistory> findByPersonIdOrderByStartDateDesc(Long personId);
}