package com.institution.management.academic_api.infra.config;

import com.institution.management.academic_api.domain.model.entities.common.SalaryStructure;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.repository.common.SalaryStructureRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(6)
@RequiredArgsConstructor
@Slf4j
public class DataSeederV6 implements CommandLineRunner {

    private final SalaryStructureRepository salaryStructureRepository;
    private final EmployeeRepository employeeRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando seeder de Dados Financeiros e de RH...");

        createSalaryStructures();
        assignSalaryStructuresToStaff();

        log.info("Seeder de Dados Financeiros e de RH finalizado.");
    }

    private void createSalaryStructures() {
        log.info("Criando estruturas salariais...");
        createSalaryStructureIfNotExists(JobPosition.TECHNICIAN, SalaryLevel.JUNIOR, new BigDecimal("3200.00"));
        createSalaryStructureIfNotExists(JobPosition.LIBRARIAN, SalaryLevel.JUNIOR, new BigDecimal("3000.00"));
        createSalaryStructureIfNotExists(JobPosition.HR_ANALYST, SalaryLevel.MID_LEVEL, new BigDecimal("5500.00"));
        createSalaryStructureIfNotExists(JobPosition.COORDINATOR, SalaryLevel.LEAD, new BigDecimal("8000.00"));


        createSalaryStructureIfNotExists(JobPosition.TEACHER, SalaryLevel.JUNIOR, new BigDecimal("4000.00"));
        createSalaryStructureIfNotExists(JobPosition.TEACHER, SalaryLevel.MID_LEVEL, new BigDecimal("6000.00"));
        createSalaryStructureIfNotExists(JobPosition.TEACHER, SalaryLevel.SENIOR, new BigDecimal("8000.00"));
    }

    private void assignSalaryStructuresToStaff() {
        log.info("Associando estruturas salariais aos funcionários e professores...");

        assignStructureToEmployees(JobPosition.TECHNICIAN, SalaryLevel.JUNIOR);

        assignStructureToEmployees(JobPosition.LIBRARIAN, SalaryLevel.JUNIOR);

        assignStructureToEmployees(JobPosition.HR_ANALYST, SalaryLevel.MID_LEVEL);

        SalaryStructure teacherStructure = salaryStructureRepository.findByJobPositionAndLevel(JobPosition.TEACHER, SalaryLevel.JUNIOR).orElse(null);
        if (teacherStructure != null) {
            List<Teacher> teachers = teacherRepository.findAll();
            teachers.forEach(teacher -> {
                if (teacher.getSalaryStructure() == null) {
                    teacher.setSalaryStructure(teacherStructure);
                    teacherRepository.save(teacher);
                    log.info("Estrutura salarial JUNIOR associada ao professor: {}", teacher.getEmail());
                }
            });
        }
    }

    private void assignStructureToEmployees(JobPosition position, SalaryLevel level) {
        SalaryStructure structure = salaryStructureRepository.findByJobPositionAndLevel(position, level).orElse(null);
        if (structure != null) {
            List<Employee> employees = employeeRepository.findByJobPosition(position);
            employees.forEach(employee -> {
                if (employee.getSalaryStructure() == null) {
                    employee.setSalaryStructure(structure);
                    employeeRepository.save(employee);
                    log.info("Estrutura salarial {} - {} associada a: {}", position, level, employee.getEmail());
                }
            });
        }
    }

    private void createSalaryStructureIfNotExists(JobPosition jobPosition, SalaryLevel level, BigDecimal baseSalary) {
        salaryStructureRepository.findByJobPositionAndLevel(jobPosition, level)
                .orElseGet(() -> {
                    log.info("Criando estrutura salarial para {} - {}", jobPosition, level);
                    SalaryStructure structure = new SalaryStructure();
                    structure.setJobPosition(jobPosition);
                    structure.setLevel(level);
                    structure.setBaseSalary(baseSalary);
                    return salaryStructureRepository.save(structure);
                });
    }
}