package com.institution.management.academic_api.infra.aplication.aop;

import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import com.institution.management.academic_api.domain.repository.common.ActivityLogRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    @AfterReturning("@annotation(logActivity)")
    public void logActivity(JoinPoint joinPoint, LogActivity logActivity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }
        String username = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (username == null || "anonymousUser".equals(username)) {
            return;
        }

        userRepository.findByLogin(username).ifPresent(user -> {
            ActivityLog log = new ActivityLog();
            log.setUser(user);
            log.setDescription(logActivity.value());
            log.setTimestamp(LocalDateTime.now());

            activityLogRepository.save(log);
        });
    }
}