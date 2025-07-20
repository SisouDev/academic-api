package com.institution.management.academic_api.infra.config;

import com.institution.management.academic_api.domain.model.entities.absence.Absence;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.absence.AbsenceStatus;
import com.institution.management.academic_api.domain.model.enums.absence.AbsenceType;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import com.institution.management.academic_api.domain.model.enums.calendar.EventType;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketCategory;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketPriority;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestType;
import com.institution.management.academic_api.domain.repository.absence.AbsenceRepository;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.announcement.AnnouncementRepository;
import com.institution.management.academic_api.domain.repository.calendar.CalendarEventRepository;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.repository.helpDesk.SupportTicketRepository;
import com.institution.management.academic_api.domain.repository.humanResources.LeaveRequestRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class DataSeederV2 implements CommandLineRunner {

    private final AbsenceRepository absenceRepository;
    private final AnnouncementRepository announcementRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    private final CalendarEventRepository calendarEventRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final StudentRepository studentRepository;
    private final PersonRepository personRepository;


    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Data Seeder V2...");

        if (isDatabaseAlreadySeeded()) {
            log.info("Data Seeder V2 tables are not empty. Skipping seeder.");
            return;
        }

        seedAbsences();
        seedAnnouncements();
        seedCalendarEvents();
        seedFinancialTransactions();
        seedLeaveRequests();
        seedSupportTickets();

        log.info("Data Seeder V2 finished successfully.");
    }

    private boolean isDatabaseAlreadySeeded() {
        return absenceRepository.count() > 0 ||
                announcementRepository.count() > 0 ||
                calendarEventRepository.count() > 0 ||
                financialTransactionRepository.count() > 0 ||
                leaveRequestRepository.count() > 0 ||
                supportTicketRepository.count() > 0;
    }

    private void seedAnnouncements() {
        log.info("Seeding announcements...");

        Employee adminCreator = employeeRepository.findAll().stream().findFirst().orElse(null);
        Department hrDepartment = departmentRepository.findByNameIgnoreCase("Recursos Humanos").orElse(null);

        if (adminCreator == null || hrDepartment == null) {
            log.warn("Could not seed announcements because initial admin or HR department was not found.");
            return;
        }

        Announcement institutionalAnnouncement = new Announcement();
        institutionalAnnouncement.setTitle("Feriado Institucional - Dia do Professor");
        institutionalAnnouncement.setContent("Em comemoração ao Dia do Professor, não haverá expediente no dia 15 de Outubro.");
        institutionalAnnouncement.setScope(AnnouncementScope.INSTITUTION);
        institutionalAnnouncement.setCreatedBy(adminCreator);
        institutionalAnnouncement.setCreatedAt(LocalDateTime.now());
        institutionalAnnouncement.setExpiresAt(LocalDateTime.now().plusMonths(1));

        Announcement departmentAnnouncement = new Announcement();
        departmentAnnouncement.setTitle("Reunião do Departamento de RH");
        departmentAnnouncement.setContent("Haverá uma reunião obrigatória para todos os membros do departamento de RH na próxima sexta-feira às 10h.");
        departmentAnnouncement.setScope(AnnouncementScope.DEPARTMENT);
        departmentAnnouncement.setTargetDepartment(hrDepartment);
        departmentAnnouncement.setCreatedBy(adminCreator);
        departmentAnnouncement.setCreatedAt(LocalDateTime.now());

        announcementRepository.saveAll(List.of(institutionalAnnouncement, departmentAnnouncement));
        log.info("Seeded 2 announcements.");
    }

    private void seedAbsences() {
        log.info("Seeding absences...");

        List<Employee> employees = employeeRepository.findAll();
        if (employees.size() < 2) {
            log.warn("Not enough employees found to seed absences.");
            return;
        }

        Employee employee1 = employees.get(0);
        Employee employee2 = employees.get(1);

        Absence absence1 = new Absence();
        absence1.setPerson(employee1);
        absence1.setType(AbsenceType.ABSENCE);
        absence1.setDate(LocalDate.now().minusDays(10));
        absence1.setJustification("Consulta médica de rotina.");
        absence1.setStatus(AbsenceStatus.APPROVED);
        absence1.setReviewedBy(employee2);
        absence1.setReviewedAt(LocalDateTime.now().minusDays(9));
        absence1.setCreatedAt(LocalDateTime.now().minusDays(10));

        Absence absence2 = new Absence();
        absence2.setPerson(employee2);
        absence2.setType(AbsenceType.LATENESS);
        absence2.setDate(LocalDate.now().minusDays(5));
        absence2.setJustification("Problemas com o transporte público.");
        absence2.setStatus(AbsenceStatus.PENDING);
        absence2.setCreatedAt(LocalDateTime.now().minusDays(5));

        absenceRepository.saveAll(List.of(absence1, absence2));
        log.info("Seeded 2 absence records.");
    }

    private void seedCalendarEvents() {
        log.info("Seeding calendar events...");
        Employee creator = employeeRepository.findAll().stream().findFirst().orElse(null);
        if (creator == null) {
            log.warn("Could not seed calendar events because no employee was found.");
            return;
        }

        CalendarEvent event1 = new CalendarEvent();
        event1.setTitle("Reunião Geral de Alinhamento");
        event1.setDescription("Reunião para discutir os resultados do último trimestre e alinhar as metas para o próximo.");
        event1.setType(EventType.MEETING);
        event1.setStartTime(LocalDateTime.now().plusDays(7).withHour(10).withMinute(0));
        event1.setEndTime(LocalDateTime.now().plusDays(7).withHour(11).withMinute(30));
        event1.setScope(AnnouncementScope.INSTITUTION);
        event1.setCreatedBy(creator);
        event1.setCreatedAt(LocalDateTime.now());

        CalendarEvent event2 = new CalendarEvent();
        event2.setTitle("Workshop de Spring Boot Avançado");
        event2.setDescription("Workshop prático sobre tópicos avançados em Spring Boot, incluindo WebFlux e segurança.");
        event2.setType(EventType.WORKSHOP);
        event2.setStartTime(LocalDateTime.now().plusDays(15).withHour(14).withMinute(0));
        event2.setEndTime(LocalDateTime.now().plusDays(15).withHour(18).withMinute(0));
        event2.setScope(AnnouncementScope.INSTITUTION);
        event2.setCreatedBy(creator);
        event2.setCreatedAt(LocalDateTime.now());

        calendarEventRepository.saveAll(List.of(event1, event2));
        log.info("Seeded 2 calendar events.");
    }

    private void seedFinancialTransactions() {
        log.info("Seeding financial transactions...");
        Student student = studentRepository.findAll().stream().findFirst().orElse(null);
        if (student == null) {
            log.warn("Could not seed financial transactions because no student was found.");
            return;
        }

        FinancialTransaction transaction1 = new FinancialTransaction();
        transaction1.setPerson(student);
        transaction1.setDescription("Pagamento da mensalidade - Mês de Julho");
        transaction1.setAmount(new BigDecimal("750.00"));
        transaction1.setType(TransactionType.PAYMENT);
        transaction1.setTransactionDate(LocalDate.now().minusDays(5));
        transaction1.setCreatedAt(LocalDateTime.now());
        transaction1.setStatus(TransactionStatus.PAID);

        FinancialTransaction transaction2 = new FinancialTransaction();
        transaction2.setPerson(student);
        transaction2.setDescription("Compra de material didático");
        transaction2.setAmount(new BigDecimal("120.50"));
        transaction2.setType(TransactionType.PAYMENT);
        transaction2.setTransactionDate(LocalDate.now().minusDays(2));
        transaction2.setCreatedAt(LocalDateTime.now());
        transaction2.setStatus(TransactionStatus.PENDING);

        financialTransactionRepository.saveAll(List.of(transaction1, transaction2));
        log.info("Seeded 2 financial transactions.");
    }

    private void seedLeaveRequests() {
        log.info("Seeding leave requests...");
        List<Employee> employees = employeeRepository.findAll();
        if (employees.size() < 2) {
            log.warn("Not enough employees found to seed leave requests.");
            return;
        }

        Employee requester = employees.get(0);
        Employee reviewer = employees.get(1);

        LeaveRequest request1 = new LeaveRequest();
        request1.setRequester(requester);
        request1.setType(LeaveRequestType.VACATION);
        request1.setStartDate(LocalDate.now().plusDays(30));
        request1.setEndDate(LocalDate.now().plusDays(45));
        request1.setReason("Férias anuais programadas.");
        request1.setStatus(LeaveRequestStatus.PENDING);
        request1.setCreatedAt(LocalDateTime.now());

        LeaveRequest request2 = new LeaveRequest();
        request2.setRequester(reviewer);
        request2.setReviewer(requester);
        request2.setType(LeaveRequestType.DAY_OFF);
        request2.setStartDate(LocalDate.now().minusDays(1));
        request2.setEndDate(LocalDate.now().minusDays(1));
        request2.setReason("Consulta médica.");
        request2.setStatus(LeaveRequestStatus.APPROVED);
        request2.setReviewedAt(LocalDateTime.now());
        request2.setCreatedAt(LocalDateTime.now().minusDays(1));

        leaveRequestRepository.saveAll(List.of(request1, request2));
        log.info("Seeded 2 leave requests.");
    }

    private void seedSupportTickets() {
        log.info("Seeding support tickets...");
        Person requester = personRepository.findAll().stream().findFirst().orElse(null);
        Employee assignee = employeeRepository.findByDepartmentName("TI").stream().findFirst().orElse(null);

        if (requester == null) {
            log.warn("Could not seed support tickets because no person was found to be a requester.");
            return;
        }

        SupportTicket ticket1 = new SupportTicket();
        ticket1.setTitle("Problema com acesso ao portal do aluno");
        ticket1.setDescription("Não consigo acessar o portal do aluno com minhas credenciais. Recebo uma mensagem de erro de 'usuário ou senha inválida'.");
        ticket1.setStatus(TicketStatus.OPEN);
        ticket1.setPriority(TicketPriority.HIGH);
        ticket1.setCategory(TicketCategory.OTHER);
        ticket1.setRequester(requester);
        ticket1.setAssignee(assignee);
        ticket1.setCreatedAt(LocalDateTime.now().minusHours(2));

        SupportTicket ticket2 = new SupportTicket();
        ticket2.setTitle("Dúvida sobre o calendário acadêmico");
        ticket2.setDescription("Gostaria de saber a data final para trancamento de matrícula neste semestre.");
        ticket2.setStatus(TicketStatus.IN_PROGRESS);
        ticket2.setPriority(TicketPriority.MEDIUM);
        ticket2.setCategory(TicketCategory.GENERAL_QUESTION);
        ticket2.setRequester(requester);
        ticket2.setCreatedAt(LocalDateTime.now().minusDays(1));

        supportTicketRepository.saveAll(List.of(ticket1, ticket2));
        log.info("Seeded 2 support tickets.");
    }
}