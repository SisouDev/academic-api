package com.institution.management.academic_api.application.service.dashboard;

import com.institution.management.academic_api.application.dto.dashboard.admin.ActivityFeedItem;
import com.institution.management.academic_api.application.dto.dashboard.admin.AdminDashboardDto;
import com.institution.management.academic_api.application.dto.dashboard.admin.GlobalStatsDto;
import com.institution.management.academic_api.application.dto.dashboard.admin.StudentDistributionData;
import com.institution.management.academic_api.application.dto.dashboard.employee.*;
import com.institution.management.academic_api.application.dto.dashboard.student.*;
import com.institution.management.academic_api.application.dto.dashboard.teacher.TeacherDashboardDto;
import com.institution.management.academic_api.application.dto.dashboard.teacher.UpcomingTaskInfo;
import com.institution.management.academic_api.application.dto.dashboard.teacher.WorkloadSummary;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import com.institution.management.academic_api.domain.model.enums.common.NotificationStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.model.enums.tasks.TaskStatus;
import com.institution.management.academic_api.domain.repository.announcement.AnnouncementRepository;
import com.institution.management.academic_api.domain.repository.calendar.CalendarEventRepository;
import com.institution.management.academic_api.domain.repository.common.NotificationRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.helpDesk.SupportTicketRepository;
import com.institution.management.academic_api.domain.repository.humanResources.LeaveRequestRepository;
import com.institution.management.academic_api.domain.repository.it.AssetRepository;
import com.institution.management.academic_api.domain.repository.library.LoanRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.AttendanceRecordRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.domain.repository.tasks.TaskRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final LoanRepository loanRepository;
    private final SupportTicketRepository ticketRepository;
    private final AssessmentDefinitionRepository assessmentDefinitionRepository;
    private final AssessmentRepository assessmentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final NotificationRepository notificationRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AssetRepository assetRepository;
    private final TaskRepository taskRepository;
    private final AnnouncementRepository announcementRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Object getDashboardDataForUser(User user) {
        Set<String> roles = user.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toSet());
        log.info("Buscando dashboard para usuário '{}' com as roles: {}", user.getUsername(), roles);

        if (roles.contains("ROLE_ADMIN")) {
            log.info("Usuário é ADMIN. Retornando AdminDashboardDto.");
            return getAdminDashboard(user);
        }
        if (roles.contains("ROLE_LIBRARIAN") || roles.contains("ROLE_TECHNICIAN") || roles.contains("ROLE_HR_ANALYST")) {
            log.info("Usuário é um Funcionário com cargo específico. Retornando EmployeeDashboardDto.");
            return getEmployeeDashboard(user);
        }
        if (roles.contains("ROLE_TEACHER")) {
            log.info("Usuário é TEACHER, retornando TeacherDashboardDto.");
            return getTeacherDashboard(user);
        }
        if (roles.contains("ROLE_STUDENT")) {
            log.info("Usuário é STUDENT, retornando StudentDashboardDto.");
            return getStudentDashboard(user);
        }
        if (roles.contains("ROLE_EMPLOYEE")) {
            log.info("Usuário é EMPLOYEE, retornando EmployeeDashboardDto.");
            return getEmployeeDashboard(user);
        }

        log.warn("Nenhum dashboard específico encontrado para o usuário '{}'. Retornando um DTO genérico.", user.getUsername());
        return new HashMap<>() ;
    }

    @Override
    public GlobalStatsDto getGlobalStats() {
        long activeStudents = studentRepository.countByStatus(PersonStatus.ACTIVE);
        long activeTeachers = teacherRepository.countByStatus(PersonStatus.ACTIVE);
        long activeLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);
        long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);

        List<AcademicTermStatus> activeTermStatuses = List.of(
                AcademicTermStatus.IN_PROGRESS,
                AcademicTermStatus.ENROLLMENT_OPEN
        );

        long activeCourses = courseRepository.countCoursesByTermStatusIn(activeTermStatuses);
        long totalUsers = userRepository.count();

        return new GlobalStatsDto(
                activeStudents,
                activeTeachers,
                activeLoans,
                openTickets,
                activeCourses,
                totalUsers
        );
    }

    private AdminDashboardDto getAdminDashboard(User user) {
        GlobalStatsDto globalStats = getGlobalStats();

        List<StudentDistributionData> studentDistribution = studentRepository.findStudentDistributionByCourse();
        List<ActivityFeedItem> recentActivities = studentRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(student -> new ActivityFeedItem(
                        "NEW_STUDENT",
                        "Novo aluno matriculado: " + student.getFirstName() + " " + student.getLastName(),
                        student.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new AdminDashboardDto(
                globalStats,
                studentDistribution,
                recentActivities
        );
    }


    private StudentDashboardDto getStudentDashboard(User user) {
        Student student = studentRepository.findById(user.getPerson().getId())
                .orElseThrow(() -> new IllegalStateException("Student not found for the authenticated user."));

        BigDecimal averageScore = assessmentRepository.findAverageScoreByStudent(student.getId());
        long totalClasses = attendanceRecordRepository.countByEnrollment_Student(student);
        long totalPresents = attendanceRecordRepository.countByEnrollment_StudentAndWasPresent(student, true);
        double attendanceRate = (totalClasses > 0) ? ((double) totalPresents / totalClasses) * 100 : 100.0;
        AcademicSummary academicSummary = new AcademicSummary(averageScore, attendanceRate);


        AssessmentDefinition nextAssessmentEntity = assessmentDefinitionRepository
                .findNextAssessmentForStudent(student, LocalDate.now())
                .orElse(null);
        UpcomingAssessmentInfo nextAssessmentInfo = (nextAssessmentEntity != null)
                ? new UpcomingAssessmentInfo(
                nextAssessmentEntity.getTitle(),
                nextAssessmentEntity.getCourseSection().getSubject().getName(),
                nextAssessmentEntity.getAssessmentDate()
        )
                : null;


        Enrollment activeEnrollment = student.getEnrollments().stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .findFirst().orElse(null);

        CourseInfo courseInfo;
        if (activeEnrollment != null) {
            CourseSection section = activeEnrollment.getCourseSection();
            String courseName = section.getSubject().getCourse().getName();

            int currentSemester = section.getAcademicTerm().getSemester();
            String conclusionForecast = (section.getAcademicTerm().getYear().getValue() + 2) + "/" + section.getAcademicTerm().getSemester();

            courseInfo = new CourseInfo(courseName, currentSemester, conclusionForecast);
        } else {
            courseInfo = new CourseInfo("Nenhum curso ativo", 0, "N/A");
        }

        List<CalendarEvent> events = calendarEventRepository.findUpcomingEvents(
                AnnouncementScope.INSTITUTION,
                student.getInstitution().getDepartments().get(0),
                LocalDateTime.now(),
                PageRequest.of(0, 5)
        );
        List<CalendarEventInfo> eventInfos = events.stream()
                .map(event -> new CalendarEventInfo(
                        event.getTitle(),
                        event.getType().toString(),
                        event.getStartTime()))
                .collect(Collectors.toList());


        return new StudentDashboardDto(
                courseInfo,
                academicSummary,
                nextAssessmentInfo,
                eventInfos
        );
    }

    private TeacherDashboardDto getTeacherDashboard(User user) {
        Teacher teacher = teacherRepository.findById(user.getPerson().getId())
                .orElseThrow(() -> new IllegalStateException("Teacher not found for authenticated user."));

        long unreadNotifications = notificationRepository.countByRecipientAndStatus(user, NotificationStatus.UNREAD);

        List<CourseSection> activeSections = courseSectionRepository.findByTeacherAndAcademicTerm_Status(teacher, AcademicTermStatus.ENROLLMENT_OPEN);
        int activeClassesCount = activeSections.size();
        int totalStudentsCount = activeSections.stream().mapToInt(section -> section.getEnrollments().size()).sum();
        long subjectsTaughtCount = activeSections.stream().map(section -> section.getSubject().getId()).distinct().count();
        WorkloadSummary workload = new WorkloadSummary(activeClassesCount, totalStudentsCount, (int) subjectsTaughtCount);

        List<UpcomingTaskInfo> upcomingTasks =assessmentDefinitionRepository.findUpcomingAssessmentsForTeacher(teacher, LocalDate.now(), PageRequest.of(0, 5))
                .stream()
                .map(ad -> new UpcomingTaskInfo(
                        ad.getTitle(),
                        "Avaliação",
                        ad.getAssessmentDate().atStartOfDay(),
                        ad.getCourseSection().getName()
                ))
                .collect(Collectors.toList());

        long pendingRequestsCount = ticketRepository.countByAssigneeAndStatus(teacher, TicketStatus.OPEN);

        return new TeacherDashboardDto(
                unreadNotifications,
                workload,
                upcomingTasks,
                pendingRequestsCount
        );
    }

    private EmployeeDashboardDto getEmployeeDashboard(User user) {
        Employee employee = employeeRepository.findById(user.getPerson().getId())
                .orElseThrow(() -> new IllegalStateException("Employee not found for authenticated user."));

        long unreadNotifications = notificationRepository.countByRecipientAndStatus(user, NotificationStatus.UNREAD);

        long pendingTasksCount = taskRepository.countByAssigneeAndStatusNot(employee, TaskStatus.IN_PROGRESS);

        List<TaskSummary> myOpenTasks = taskRepository.findTop5ByAssigneeAndStatusNotOrderByDueDateAsc(employee, TaskStatus.DONE)
                .stream()
                .map(task -> new TaskSummary(task.getId(), task.getTitle(), task.getDueDate(), task.getStatus().name()))
                .collect(Collectors.toList());

        List<AnnouncementSummaryDto> recentAnnouncements = announcementRepository.findTop3ByOrderByCreatedAtDesc()
                .stream()
                .map(a -> new AnnouncementSummaryDto(a.getId(), a.getTitle(), a.getScope().name(), a.getCreatedAt()))
                .collect(Collectors.toList());

        LibrarianSummaryDto librarianInfo = null;
        TechnicianSummaryDto technicianInfo = null;
        HrAnalystSummaryDto hrAnalystInfo = null;

        if (employee.getJobPosition() == JobPosition.LIBRARIAN) {
            long pendingLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);
            long overdueBooks = loanRepository.countByStatusAndReturnDateBefore(LoanStatus.OVERDUE, LocalDate.now());
            librarianInfo = new LibrarianSummaryDto(pendingLoans, overdueBooks);
        }

        if (employee.getJobPosition() == JobPosition.TECHNICIAN) {
            long openTickets = supportTicketRepository.countByAssigneeAndStatus(employee, TicketStatus.OPEN);
            long assignedAssets = assetRepository.countByAssignedTo(employee);
            technicianInfo = new TechnicianSummaryDto(openTickets, assignedAssets);
        }

        if (employee.getJobPosition() == JobPosition.HR_ANALYST) {
            long pendingLeaveRequests = leaveRequestRepository.countByStatus(LeaveRequestStatus.PENDING);

            LocalDate startOfMonth = YearMonth.now().atDay(1);
            long newHires = employeeRepository.countByHiringDateAfter(startOfMonth);

            List<LeaveRequestSummary> recentLeaveRequests = leaveRequestRepository.findTop5ByStatusOrderByCreatedAtDesc(LeaveRequestStatus.PENDING)
                    .stream()
                    .map(lr -> new LeaveRequestSummary(
                            lr.getId(),
                            lr.getRequester().getFirstName() + " " + lr.getRequester().getLastName(),
                            lr.getType().name(),
                            lr.getStartDate(),
                            lr.getEndDate(),
                            lr.getStatus().name()
                    ))
                    .collect(Collectors.toList());

            hrAnalystInfo = new HrAnalystSummaryDto(pendingLeaveRequests, newHires, recentLeaveRequests);
        }

        return new EmployeeDashboardDto(
                unreadNotifications,
                pendingTasksCount,
                myOpenTasks,
                recentAnnouncements,
                librarianInfo,
                technicianInfo,
                hrAnalystInfo
        );
    }
}
