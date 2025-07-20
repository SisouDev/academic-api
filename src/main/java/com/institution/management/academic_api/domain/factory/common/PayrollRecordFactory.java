package com.institution.management.academic_api.domain.factory.common;

import com.institution.management.academic_api.domain.model.entities.common.PayrollRecord;
import com.institution.management.academic_api.domain.model.entities.common.StaffMember;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PayrollRecordFactory {

    public PayrollRecord create(StaffMember staffMember) {
        if (staffMember.getSalaryStructure() == null) {
            throw new IllegalStateException("O funcionário " + staffMember.getEmail() + " não possui uma estrutura salarial associada.");
        }

        PayrollRecord record = new PayrollRecord();
        record.setPerson(staffMember);
        record.setReferenceMonth(LocalDate.now().withDayOfMonth(1));

        record.setBaseSalary(staffMember.getSalaryStructure().getBaseSalary());

        record.setBonuses(null);
        record.setDeductions(null);

        record.setNetPay(staffMember.getSalaryStructure().getBaseSalary());

        record.setStatus(PayrollStatus.PENDING);
        record.setPaymentDate(null);
        record.setCreatedAt(LocalDateTime.now());

        return record;
    }
}