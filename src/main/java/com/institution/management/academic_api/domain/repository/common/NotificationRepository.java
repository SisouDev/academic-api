package com.institution.management.academic_api.domain.repository.common;

import com.institution.management.academic_api.domain.model.entities.common.Notification;
import com.institution.management.academic_api.domain.model.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
}