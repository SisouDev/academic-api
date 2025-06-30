package com.institution.management.academic_api.application.controller.employee;

import com.institution.management.academic_api.application.dto.course.CourseStudentCountDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.service.employee.AdminService;
import com.institution.management.academic_api.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/dashboard-stats")
    public ResponseEntity<InstitutionDetailsDto> getDashboardStats() {
        InstitutionDetailsDto stats = adminService.getDashboardStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/student-distribution")
    public ResponseEntity<List<CourseStudentCountDto>> getStudentDistribution() {
        List<CourseStudentCountDto> distributionData = adminService.getStudentDistribution();
        return ResponseEntity.ok(distributionData);
    }

    @PostMapping("/users/{userId}/reset-password")
    public ResponseEntity<Void> resetUserPassword(@PathVariable Long userId) {
        userService.adminResetPassword(userId);
        return ResponseEntity.noContent().build();
    }
}