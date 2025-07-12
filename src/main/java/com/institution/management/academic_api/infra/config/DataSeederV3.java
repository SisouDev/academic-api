package com.institution.management.academic_api.infra.config;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.inventory.Material;
import com.institution.management.academic_api.domain.model.entities.it.Asset;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import com.institution.management.academic_api.domain.model.enums.inventory.MaterialType;
import com.institution.management.academic_api.domain.model.enums.it.AssetStatus;
import com.institution.management.academic_api.domain.model.enums.library.LibraryItemType;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.model.enums.library.ReservationStatus;
import com.institution.management.academic_api.domain.model.enums.meeting.MeetingVisibility;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import com.institution.management.academic_api.domain.model.enums.request.RequestType;
import com.institution.management.academic_api.domain.model.enums.request.UrgencyLevel;
import com.institution.management.academic_api.domain.model.enums.tasks.TaskStatus;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.inventory.MaterialRepository;
import com.institution.management.academic_api.domain.repository.it.AssetRepository;
import com.institution.management.academic_api.domain.repository.library.LibraryItemRepository;
import com.institution.management.academic_api.domain.repository.library.LoanRepository;
import com.institution.management.academic_api.domain.repository.library.ReservationRepository;
import com.institution.management.academic_api.domain.repository.meeting.MeetingRepository;
import com.institution.management.academic_api.domain.repository.request.InternalRequestRepository;
import com.institution.management.academic_api.domain.repository.tasks.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class DataSeederV3 implements CommandLineRunner {

    private final MaterialRepository materialRepository;
    private final AssetRepository assetRepository;
    private final LibraryItemRepository libraryItemRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final MeetingRepository meetingRepository;
    private final InternalRequestRepository internalRequestRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Data Seeder V3...");

        if (isDatabaseAlreadySeeded()) {
            log.info("Data Seeder V3 tables are not empty. Skipping seeder.");
            return;
        }
        seedMaterials();
        seedAssets();
        seedLibraryItems();
        seedLoansAndReservations();
        seedMeetings();
        seedInternalRequests();
        seedTasks();

        log.info("Data Seeder V3 finished successfully.");
    }

    private boolean isDatabaseAlreadySeeded() {
        return materialRepository.count() > 0 ||
                assetRepository.count() > 0 ||
                libraryItemRepository.count() > 0;
    }

    private void seedMaterials() {
        log.info("Seeding materials...");
        Material material1 = new Material();
        material1.setName("Resma de Papel A4");
        material1.setDescription("Caixa com 10 resmas de papel sulfite A4, 75g/m².");
        material1.setType(MaterialType.TEACHING_SUPPLIES);

        Material material2 = new Material();
        material2.setName("Kit de Limpeza");
        material2.setDescription("Kit contendo detergente, desinfetante e álcool em gel.");
        material2.setType(MaterialType.CLEANING);

        materialRepository.saveAll(List.of(material1, material2));
        log.info("Seeded 2 materials.");
    }

    private void seedAssets() {
        log.info("Seeding IT assets...");
        Employee employee = employeeRepository.findAll().stream().findFirst().orElse(null);

        Asset asset1 = new Asset();
        asset1.setName("Notebook Dell Latitude 5420");
        asset1.setAssetTag("NT-00123");
        asset1.setSerialNumber("SN-987654321");
        asset1.setPurchaseDate(LocalDate.now().minusMonths(6));
        asset1.setPurchaseCost(new BigDecimal("6500.00"));
        asset1.setStatus(AssetStatus.IN_USE);
        asset1.setAssignedTo(employee);
        asset1.setCreatedAt(LocalDateTime.now());

        Asset asset2 = new Asset();
        asset2.setName("Monitor LG 24 Polegadas");
        asset2.setAssetTag("MN-00456");
        asset2.setStatus(AssetStatus.IN_STOCK);
        asset2.setCreatedAt(LocalDateTime.now());

        assetRepository.saveAll(List.of(asset1, asset2));
        log.info("Seeded 2 IT assets.");
    }

    private void seedLibraryItems() {
        log.info("Seeding library items...");
        LibraryItem item1 = new LibraryItem();
        item1.setTitle("Java Efetivo, 3ª Edição");
        item1.setAuthor("Joshua Bloch");
        item1.setIsbn("978-8550804629");
        item1.setPublisher("Alta Books");
        item1.setPublicationYear(2018);
        item1.setType(LibraryItemType.BOOK);
        item1.setTotalCopies(5);
        item1.setAvailableCopies(5);

        LibraryItem item2 = new LibraryItem();
        item2.setTitle("Código Limpo: Habilidades Práticas do Agile Software");
        item2.setAuthor("Robert C. Martin");
        item2.setIsbn("978-8576082675");
        item2.setPublisher("Alta Books");
        item2.setPublicationYear(2009);
        item2.setType(LibraryItemType.BOOK);
        item2.setTotalCopies(8);
        item2.setAvailableCopies(8);

        libraryItemRepository.saveAll(List.of(item1, item2));
        log.info("Seeded 2 library items.");
    }

    private void seedLoansAndReservations() {
        log.info("Seeding loans and reservations...");
        LibraryItem item = libraryItemRepository.findByIsbn("978-8576082675").orElse(null);
        Person borrower = personRepository.findAll().stream().findFirst().orElse(null);

        if (item == null || borrower == null) {
            log.warn("Could not seed loans or reservations because library item or person was not found.");
            return;
        }

        // Criar um empréstimo
        Loan loan = new Loan();
        loan.setItem(item);
        loan.setBorrower(borrower);
        loan.setLoanDate(LocalDate.now().minusDays(10));
        loan.setDueDate(LocalDate.now().plusDays(4));
        loan.setStatus(LoanStatus.ACTIVE);
        loanRepository.save(loan);

        // Atualizar cópias disponíveis
        item.setAvailableCopies(item.getAvailableCopies() - 1);
        libraryItemRepository.save(item);

        log.info("Seeded 1 loan.");

        // Criar uma reserva para o mesmo item
        Person anotherPerson = personRepository.findAll().get(1);
        if (anotherPerson != null) {
            Reservation reservation = new Reservation();
            reservation.setItem(item);
            reservation.setPerson(anotherPerson);
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setStatus(ReservationStatus.ACTIVE);
            reservationRepository.save(reservation);
            log.info("Seeded 1 reservation.");
        }
    }

    private void seedMeetings() {
        log.info("Seeding meetings...");
        List<Person> people = personRepository.findAll();
        if (people.isEmpty()) {
            log.warn("Could not seed meetings because no people were found.");
            return;
        }
        Person organizer = people.get(0);
        Set<Person> participants = new HashSet<>(people.subList(0, Math.min(people.size(), 3)));

        Meeting meeting = new Meeting();
        meeting.setTitle("Reunião de Planejamento do Projeto 'Phoenix'");
        meeting.setDescription("Alinhamento das próximas sprints e definição de prioridades.");
        meeting.setStartTime(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0));
        meeting.setEndTime(LocalDateTime.now().plusDays(2).withHour(15).withMinute(30));
        meeting.setVisibility(MeetingVisibility.PUBLIC);
        meeting.setOrganizer(organizer);
        meeting.setParticipants(participants);
        meeting.setCreatedAt(LocalDateTime.now());

        meetingRepository.save(meeting);
        log.info("Seeded 1 meeting.");
    }

    private void seedInternalRequests() {
        log.info("Seeding internal requests...");
        Person requester = personRepository.findAll().stream().findFirst().orElse(null);
        Department targetDepartment = departmentRepository.findByNameIgnoreCase("TI")
                .orElse(departmentRepository.findAll().stream().findFirst().orElse(null));

        if (requester == null || targetDepartment == null) {
            log.warn("Could not seed internal requests. Requester or target department not found.");
            return;
        }

        InternalRequest request = new InternalRequest();
        request.setTitle("Solicitação de Acesso à Pasta de Rede");
        request.setDescription("Preciso de acesso de leitura e escrita à pasta de rede '//servidor/projetos' para o projeto Alpha.");
        request.setType(RequestType.HR_REQUEST);
        request.setStatus(RequestStatus.PENDING);
        request.setUrgency(UrgencyLevel.MEDIUM);
        request.setRequester(requester);
        request.setTargetDepartment(targetDepartment);
        request.setCreatedAt(LocalDateTime.now());

        internalRequestRepository.save(request);
        log.info("Seeded 1 internal request.");
    }

    private void seedTasks() {
        log.info("Seeding tasks...");
        List<Employee> employees = employeeRepository.findAll();
        Department itDepartment = departmentRepository.findByNameIgnoreCase("TI")
                .orElse(departmentRepository.findAll().stream().findFirst().orElse(null));

        if (employees.size() < 2 || itDepartment == null) {
            log.warn("Could not seed tasks. Not enough employees or IT department not found.");
            return;
        }

        Employee creator = employees.get(0);
        Employee assignee = employees.get(1);

        Task task = new Task();
        task.setTitle("Preparar ambiente de desenvolvimento para novo estagiário");
        task.setDescription("Instalar JDK, Maven, IntelliJ e configurar acesso ao Git.");
        task.setDueDate(LocalDate.now().plusDays(3));
        task.setStatus(TaskStatus.TODO);
        task.setAssignee(assignee);
        task.setDepartment(itDepartment);
        task.setCreatedBy(creator);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);
        log.info("Seeded 1 task.");
    }
}