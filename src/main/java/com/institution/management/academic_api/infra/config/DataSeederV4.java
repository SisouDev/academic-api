package com.institution.management.academic_api.infra.config;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
@Order(4)
public class DataSeederV4 implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final InstitutionRepository institutionRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Executando DataSeederV4 para o perfil 'dev'...");
        Institution defaultInstitution = institutionRepository.findByRegisterId("00.000.000/0001-00")
                .orElse(null);

        if (defaultInstitution == null) {
            log.warn("Instituição padrão não encontrada. Novos funcionários não serão criados.");
            return;
        }

        seedNewRoles();
        seedNewEmployees(defaultInstitution);

        log.info("DataSeederV4 finalizado com sucesso!");
    }

    private void seedNewRoles() {
        log.info("Verificando e criando novos perfis (Roles)...");
        createRoleIfNotExists(RoleName.ROLE_LIBRARIAN);
        createRoleIfNotExists(RoleName.ROLE_TECHNICIAN);
        createRoleIfNotExists(RoleName.ROLE_HR_ANALYST);
    }

    private void createRoleIfNotExists(RoleName roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(new Role(roleName));
            log.info("Role '{}' criada com sucesso.", roleName);
        }
    }

    private Map<String, Employee> seedNewEmployees(Institution institution) {
        log.info("Iniciando seeding de Novos Funcionários...");
        Map<String, Employee> createdEmployees = new HashMap<>();

        createdEmployees.put("LIBRARIAN_USER", createEmployeeIfNotExists(
                "Beatriz",
                "Costa",
                "beatriz.costa@instituicao.com",
                JobPosition.LIBRARIAN,
                LocalDate.of(2024, 3, 15),
                new DocumentDto("NATIONAL_ID", "10110110101"),
                institution
        ));

        createdEmployees.put("TECH_USER", createEmployeeIfNotExists(
                "Carlos",
                "Andrade",
                "carlos.andrade@instituicao.com",
                JobPosition.TECHNICIAN,
                LocalDate.of(2024, 2, 10),
                new DocumentDto("NATIONAL_ID", "20220220202"),
                institution
        ));

        createdEmployees.put("HR_USER", createEmployeeIfNotExists(
                "Fernanda",
                "Lima",
                "fernanda.lima@instituicao.com",
                JobPosition.HR_ANALYST,
                LocalDate.of(2024, 1, 20),
                new DocumentDto("NATIONAL_ID", "30330330303"),
                institution
        ));

        log.info("Seeding de Novos Funcionários finalizado.");
        return createdEmployees;
    }

    private Employee createEmployeeIfNotExists(String firstName, String lastName, String email, JobPosition position, LocalDate hiringDate, DocumentDto document, Institution institution) {
        Optional<Person> existingPerson = personRepository.findByEmail(email);
        if (existingPerson.isPresent()) {
            log.warn("Uma pessoa com o e-mail {} já existe. Nenhum funcionário novo foi criado.", email);
            if (existingPerson.get() instanceof Employee) {
                return (Employee) existingPerson.get();
            }
            return null;
        }

        Set<RoleName> rolesForEmployee = new HashSet<>();
        rolesForEmployee.add(RoleName.ROLE_EMPLOYEE);

        switch (position) {
            case LIBRARIAN:
                rolesForEmployee.add(RoleName.ROLE_LIBRARIAN);
                break;
            case TECHNICIAN:
                rolesForEmployee.add(RoleName.ROLE_TECHNICIAN);
                break;
            case HR_ANALYST:
                rolesForEmployee.add(RoleName.ROLE_HR_ANALYST);
                break;
        }

        var employeeRequest = new CreateEmployeeRequestDto(
                firstName,
                lastName,
                email,
                position.getDisplayName(),
                hiringDate,
                institution.getId(),
                1L,
                document,
                rolesForEmployee
        );

        try {
            EmployeeResponseDto createdEmployeeDto = employeeService.createEmployee(employeeRequest);
            log.info("Funcionário '{}' e seu usuário correspondente foram criados com sucesso. A senha é o número do documento.", createdEmployeeDto.getEmail());

            return employeeRepository.findById(createdEmployeeDto.getId())
                    .orElseThrow(() -> new IllegalStateException("O funcionário acabou de ser criado, mas não foi encontrado."));
        } catch (Exception e) {
            log.error("Erro ao criar o funcionário de exemplo '{}': {}", email, e.getMessage(), e);
            return null;
        }
    }

}
