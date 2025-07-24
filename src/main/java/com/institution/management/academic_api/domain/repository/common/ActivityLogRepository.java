package com.institution.management.academic_api.domain.repository.common;

import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop5ByUserIdOrderByTimestampDesc(Long userId);
    List<ActivityLog> findTop5ByOrderByTimestampDesc();

    List<ActivityLog> findTop10ByOrderByTimestampDesc();
}