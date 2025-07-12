package com.institution.management.academic_api.domain.service.common;

import com.institution.management.academic_api.domain.model.entities.user.User;

import java.util.List;

public interface NotificationAudienceService {
    List<User> getUsersForInstitutionalScope();
    List<User> getUsersForDepartmentScope(Long departmentId);
}
