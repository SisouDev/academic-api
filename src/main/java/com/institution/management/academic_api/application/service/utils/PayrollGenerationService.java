package com.institution.management.academic_api.application.service.utils;

import com.institution.management.academic_api.domain.factory.common.PayrollRecordFactory;
import com.institution.management.academic_api.domain.model.entities.common.PayrollRecord;
import com.institution.management.academic_api.domain.model.entities.common.StaffMember;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.repository.common.PayrollRecordRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollGenerationService {

    private final EmployeeRepository employeeRepository;
    private final TeacherRepository teacherRepository;
    private final PayrollRecordRepository payrollRecordRepository;
    private final PayrollRecordFactory payrollRecordFactory;
    private final BonusCalculationService bonusCalculationService;

    @Scheduled(cron = "0 0 3 1 * ?")
    @Transactional
    public void generateMonthlyPayroll() {
        log.info("JOB START: Iniciando geração da folha de pagamento mensal...");
        LocalDate referenceMonth = LocalDate.now().withDayOfMonth(1);

        List<StaffMember> activeStaff = new ArrayList<>();
        activeStaff.addAll(employeeRepository.findAllByStatus(PersonStatus.ACTIVE));
        activeStaff.addAll(teacherRepository.findAllByStatus(PersonStatus.ACTIVE));

        log.info("Encontrados {} membros da equipe ativos para processar.", activeStaff.size());

        for (StaffMember staffMember : activeStaff) {
            if (payrollRecordRepository.existsByPersonIdAndReferenceMonth(staffMember.getId(), referenceMonth)) {
                log.warn("Folha de pagamento para {} ({}) no mês {} já existe. Pulando.", staffMember.getEmail(), staffMember.getId(), referenceMonth.getMonth());
                continue;
            }

            PayrollRecord record = payrollRecordFactory.create(staffMember);

            BigDecimal bonuses = bonusCalculationService.calculateBonuses(staffMember);
            if (bonuses.compareTo(BigDecimal.ZERO) > 0) {
                record.setBonuses(bonuses);
            }

            BigDecimal netPay = record.getBaseSalary().add(record.getBonuses() != null ? record.getBonuses() : BigDecimal.ZERO);
            record.setNetPay(netPay);

            payrollRecordRepository.save(record);
            log.info("Folha de pagamento gerada com sucesso para: {}", staffMember.getEmail());
        }
        log.info("JOB END: Geração da folha de pagamento mensal finalizada.");
    }
}