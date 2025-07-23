package com.institution.management.academic_api.application.service.dashboard;

import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;
import com.institution.management.academic_api.application.dto.dashboard.admin.ActivityFeedItem;
import com.institution.management.academic_api.application.dto.dashboard.admin.AdminDashboardDto;
import com.institution.management.academic_api.application.dto.dashboard.admin.GlobalStatsDto;
import com.institution.management.academic_api.application.dto.dashboard.admin.StudentDistributionData;
import com.institution.management.academic_api.application.dto.dashboard.employee.*;
import com.institution.management.academic_api.application.dto.dashboard.student.*;
import com.institution.management.academic_api.application.dto.dashboard.teacher.TeacherDashboardDto;
import com.institution.management.academic_api.application.dto.dashboard.teacher.UpcomingTaskInfo;
import com.institution.management.academic_api.application.dto.dashboard.teacher.WorkloadSummary;
import com.institution.management.academic_api.application.dto.request.InternalRequestSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.calendar.CalendarEventMapper;
import com.institution.management.academic_api.application.mapper.simple.request.InternalRequestMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
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
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.model.enums.tasks.TaskStatus;
import com.institution.management.academic_api.domain.repository.announcement.AnnouncementRepository;
import com.institution.management.academic_api.domain.repository.calendar.CalendarEventRepository;
import com.institution.management.academic_api.domain.repository.common.NotificationRepository;
import com.institution.management.academic_api.domain.repository.common.PayrollRecordRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.repository.financial.PurchaseOrderRepository;
import com.institution.management.academic_api.domain.repository.helpDesk.SupportTicketRepository;
import com.institution.management.academic_api.domain.repository.humanResources.LeaveRequestRepository;
import com.institution.management.academic_api.domain.repository.it.AssetRepository;
import com.institution.management.academic_api.domain.repository.library.LoanRepository;
import com.institution.management.academic_api.domain.repository.request.InternalRequestRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.AttendanceRecordRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.domain.repository.tasks.TaskRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.dashboard.DashboardService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.institution.management.academic_api.application.dto.dashboard.student.CalendarEventInfo;

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
    private final FinancialTransactionRepository financialTransactionRepository;
    private final PayrollRecordRepository payrollRecordRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InternalRequestRepository internalRequestRepository;
    private final InternalRequestMapper internalRequestMapper;
    private final CalendarEventMapper calendarEventMapper;

    @Override
    @Transactional(readOnly = true)
    public Object getDashboardDataForUser(User user) {
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        log.info("Buscando dashboard para usuário '{}' com as roles: {}", user.getUsername(), roles);

        if (roles.contains("ROLE_ADMIN")) {
            log.info("Usuário é ADMIN. Retornando AdminDashboardDto.");
            return getAdminDashboard(user);
        }

        if (roles.contains("ROLE_HR_ANALYST")) {
            log.info("Usuário é HR_ANALYST. Retornando HrAnalystDashboardDto.");
            return getHrAnalystDashboard(user);
        }

        if (roles.contains("ROLE_TECHNICIAN")) {
            log.info("Usuário é TECHNICIAN. Retornando TechnicianDashboardDto.");
            return getTechnicianDashboard(user);
        }

        if (roles.contains("ROLE_LIBRARIAN")) {
            log.info("Usuário é LIBRARIAN. Retornando EmployeeDashboardDto.");
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
        if (roles.contains("ROLE_FINANCE_MANAGER")){
            log.info("Usuário é FINANCE, retornando FinanceDashboardDto.");
            return getFinanceDashboard(user);
        }
        if (roles.contains("ROLE_FINANCE_ASSISTANT")){
            log.info("Usuário é FINANCE ASSISTANT, retornando FinanceDashboardDto.");
            return getFinanceDashboard(user);
        }

        if (roles.contains("ROLE_SECRETARY")){
            log.info("Usuário é Secretário(a), retornando SecretaryDashboardDto.");
            return getSecretaryDashboard(user);
        }

        if (roles.contains("ROLE_EMPLOYEE")) {
            log.info("Usuário é EMPLOYEE genérico, retornando EmployeeDashboardDto.");
            return getEmployeeDashboard(user);
        }

        log.warn("Nenhum dashboard específico encontrado para o usuário '{}'. Retornando um DTO genérico.", user.getUsername());
        return new HashMap<>();
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

    @Override
    @Transactional(readOnly = true)
    public HrAnalystDashboardDto getHrAnalystDashboard(User user) {
        long unreadNotifications = notificationRepository.countByRecipientAndStatus(user, NotificationStatus.UNREAD);
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

        return new HrAnalystDashboardDto(unreadNotifications, pendingLeaveRequests, newHires, recentLeaveRequests);
    }

    private EmployeeDashboardDto getEmployeeDashboard(User user) {
        CommonEmployeeData commonData = getCommonEmployeeData(user);

        LibrarianSummaryDto librarianInfo = null;
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"))) {
            long pendingLoans = loanRepository.countByStatus(LoanStatus.PENDING);
            long overdueBooks = loanRepository.countByStatus(LoanStatus.OVERDUE);
            librarianInfo = new LibrarianSummaryDto(pendingLoans, overdueBooks);
        }

        return new EmployeeDashboardDto(
                commonData.unreadNotifications(),
                commonData.pendingTasksCount(),
                commonData.myOpenTasks(),
                commonData.recentAnnouncements(),
                librarianInfo,
                null,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TechnicianDashboardDto getTechnicianDashboard(User user) {
        CommonEmployeeData commonData = getCommonEmployeeData(user);
        Employee employee = employeeRepository.findById(user.getPerson().getId())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado para o usuário autenticado."));

        long openTickets = supportTicketRepository.countByAssigneeAndStatus(employee, TicketStatus.OPEN);
        long assignedAssets = assetRepository.countByAssignedTo(employee);

        return new TechnicianDashboardDto(
                commonData.unreadNotifications(),
                commonData.pendingTasksCount(),
                commonData.myOpenTasks(),
                commonData.recentAnnouncements(),
                openTickets,
                assignedAssets
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LibrarianDashboardDto getLibrarianDashboard(User user) {
        CommonEmployeeData commonData = getCommonEmployeeData(user);

        long pendingLoans = loanRepository.countByStatus(LoanStatus.PENDING);
        long overdueBooks = loanRepository.countByStatus(LoanStatus.OVERDUE);
        long unpaidFines = financialTransactionRepository.countByTypeAndStatus(TransactionType.FINE, TransactionStatus.PENDING);


        return new LibrarianDashboardDto(
                commonData.unreadNotifications(),
                commonData.pendingTasksCount(),
                commonData.myOpenTasks(),
                commonData.recentAnnouncements(),
                pendingLoans,
                overdueBooks,
                unpaidFines
        );
    }

    private CommonEmployeeData getCommonEmployeeData(User user) {
        Employee employee = employeeRepository.findById(user.getPerson().getId())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado para o usuário autenticado."));

        long unreadNotifications = notificationRepository.countByRecipientAndStatus(user, NotificationStatus.UNREAD);
        long pendingTasksCount = taskRepository.countByAssigneeAndStatusNot(employee, TaskStatus.DONE);

        List<TaskSummary> myOpenTasks = taskRepository.findTop5ByAssigneeAndStatusNotOrderByDueDateAsc(employee, TaskStatus.DONE)
                .stream()
                .map(task -> new TaskSummary(task.getId(), task.getTitle(), task.getDueDate(), task.getStatus().name()))
                .collect(Collectors.toList());

        List<AnnouncementSummaryDto> recentAnnouncements = announcementRepository.findTop3ByOrderByCreatedAtDesc()
                .stream()
                .map(a -> new AnnouncementSummaryDto(
                        a.getId(),
                        a.getTitle(),
                        a.getCreatedAt(),
                        a.getCreatedBy().getFirstName() + " " + a.getCreatedBy().getLastName(),
                        a.getScope().name()))
                .collect(Collectors.toList());

        return new CommonEmployeeData(unreadNotifications, pendingTasksCount, myOpenTasks, recentAnnouncements);
    }

    @Override
    @Transactional(readOnly = true)
    public FinanceDashboardDto getFinanceDashboard(User user) {
        CommonEmployeeData commonData = getCommonEmployeeData(user);

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal receivable = financialTransactionRepository
                .calculateTotalAmountByTypeInDateRange(TransactionType.TUITION, startOfMonth, endOfMonth)
                .orElse(BigDecimal.ZERO);

        BigDecimal payable = payrollRecordRepository
                .calculateTotalNetPayByStatusAndDateRange(PayrollStatus.PENDING, startOfMonth, endOfMonth)
                .orElse(BigDecimal.ZERO);

        long pendingPayrolls = payrollRecordRepository.countByStatus(PayrollStatus.PENDING);
        long pendingPOs = purchaseOrderRepository.countByStatus(OrderStatus.PENDING_APPROVAL);

        return new FinanceDashboardDto(
                commonData.unreadNotifications(),
                commonData.pendingTasksCount(),
                commonData.myOpenTasks(),
                commonData.recentAnnouncements(),
                receivable,
                payable,
                pendingPayrolls,
                pendingPOs
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SecretaryDashboardDto getSecretaryDashboard(User user) {
        CommonEmployeeData commonData = getCommonEmployeeData(user);
        Employee employee = (Employee) user.getPerson();
        Department department = employee.getDepartment();

        if (department == null) {
            throw new IllegalStateException("Secretário(a) não está associado(a) a um departamento.");
        }

        long pendingRequests = internalRequestRepository.countByTargetDepartmentAndStatus(department, RequestStatus.PENDING);

        List<InternalRequestSummaryDto> recentRequests = internalRequestRepository
                .findTop5ByTargetDepartmentAndStatusOrderByCreatedAtDesc(department, RequestStatus.PENDING)
                .stream()
                .map(internalRequestMapper::toSummaryDto)
                .collect(Collectors.toList());

        List<CalendarEvent> upcomingEvents = calendarEventRepository.findUpcomingEvents(
                AnnouncementScope.INSTITUTION,
                department,
                LocalDateTime.now(),
                PageRequest.of(0, 1)
        );

        CalendarEventInfo nextEvent = upcomingEvents.isEmpty()
                ? null
                : calendarEventMapper.toInfo(upcomingEvents.getFirst());


        return new SecretaryDashboardDto(
                commonData.unreadNotifications(),
                commonData.pendingTasksCount(),
                commonData.myOpenTasks(),
                commonData.recentAnnouncements(),
                pendingRequests,
                recentRequests,
                nextEvent
        );
    }
}
