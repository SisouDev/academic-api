package com.institution.management.academic_api.application.service.utils;

import com.institution.management.academic_api.application.notifiers.financial.FinancialTransactionNotifier;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.entities.financial.Scholarship;
import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.financial.DiscountType;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.repository.financial.ScholarshipRepository;
import com.institution.management.academic_api.domain.repository.library.LoanRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksService {

    private final LoanRepository loanRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final FinancialTransactionNotifier financialTransactionNotifier;
    private final StudentRepository studentRepository;
    private final ScholarshipRepository scholarshipRepository;

    private static final BigDecimal LATE_FEE_AMOUNT = new BigDecimal("5.00");
    private static final BigDecimal MONTHLY_TUITION_FEE = new BigDecimal("850.00");

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void processOverdueLoans() {
        log.info("Iniciando job para processar empréstimos atrasados...");

        List<Loan> overdueLoans = loanRepository.findByStatusAndDueDateBefore(LoanStatus.ACTIVE, LocalDate.now());

        if (overdueLoans.isEmpty()) {
            log.info("Nenhum empréstimo atrasado encontrado. Job finalizado.");
            return;
        }

        log.info("Encontrados {} empréstimos para marcar como atrasados.", overdueLoans.size());

        for (Loan loan : overdueLoans) {
            loan.setStatus(LoanStatus.OVERDUE);
            loanRepository.save(loan);
            log.info("Empréstimo ID {} para o item '{}' foi marcado como OVERDUE.", loan.getId(), loan.getLibraryItem().getTitle());

            FinancialTransaction fine = new FinancialTransaction();
            fine.setPerson(loan.getBorrower());
            fine.setAmount(LATE_FEE_AMOUNT);
            fine.setType(TransactionType.FINE);
            fine.setStatus(TransactionStatus.PENDING);
            fine.setDescription("Multa por atraso na devolução do item: " + loan.getLibraryItem().getTitle());
            fine.setTransactionDate(LocalDate.now());
            fine.setCreatedAt(LocalDateTime.now());

            FinancialTransaction savedFine = financialTransactionRepository.save(fine);
            log.info("Multa ID {} no valor de R$ {} criada para o aluno '{}'.", savedFine.getId(), LATE_FEE_AMOUNT, loan.getBorrower().getFirstName());

            financialTransactionNotifier.notifyStudentOfNewTransaction(savedFine);
        }

        log.info("Processamento de empréstimos atrasados finalizado com sucesso.");
    }

    @Scheduled(cron = "0 0 5 5 * ?")
    @Transactional
    public void generateMonthlyTuitionFees() {
        log.info("JOB START: Iniciando geração de mensalidades...");
        List<Student> activeStudents = studentRepository.findAllByStatus(PersonStatus.ACTIVE);

        if (activeStudents.isEmpty()) {
            log.info("Nenhum aluno ativo encontrado. Job finalizado.");
            return;
        }

        log.info("Encontrados {} alunos ativos para gerar mensalidade.", activeStudents.size());
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        String monthYearDescription = today.format(DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR")));

        for (Student student : activeStudents) {
            boolean alreadyExists = financialTransactionRepository.existsByPersonAndTypeAndTransactionDateBetween(
                    student, TransactionType.TUITION, startOfMonth, endOfMonth);

            if (alreadyExists) {
                log.warn("Mensalidade para {} no mês {} já existe. Pulando.", student.getEmail(), today.getMonth());
                continue;
            }

            Enrollment activeEnrollment = student.getEnrollments().stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                    .findFirst()
                    .orElse(null);

            if (activeEnrollment == null || activeEnrollment.getCourseSection().getSubject().getCourse() == null) {
                log.error("Aluno {} não possui matrícula ativa ou curso associado. Não é possível gerar mensalidade.", student.getEmail());
                continue;
            }

            BigDecimal baseTuitionFee = activeEnrollment.getCourseSection().getSubject().getCourse().getTuitionFee();
            BigDecimal finalTuitionFee = baseTuitionFee;
            String description = "Mensalidade referente a " + monthYearDescription;

            Optional<Scholarship> scholarshipOpt = scholarshipRepository.findActiveForEnrollment(activeEnrollment, today);

            if (scholarshipOpt.isPresent()) {
                Scholarship scholarship = scholarshipOpt.get();
                log.info("Aplicando bolsa '{}' para o aluno {}", scholarship.getName(), student.getEmail());

                if (scholarship.getDiscountType() == DiscountType.PERCENTAGE) {
                    BigDecimal discountFactor = scholarship.getValue().divide(new BigDecimal("100"));
                    BigDecimal discountAmount = baseTuitionFee.multiply(discountFactor);
                    finalTuitionFee = baseTuitionFee.subtract(discountAmount);
                } else if (scholarship.getDiscountType() == DiscountType.FIXED_AMOUNT) {
                    finalTuitionFee = baseTuitionFee.subtract(scholarship.getValue());
                }

                description += String.format(" (Bolsa '%s' aplicada)", scholarship.getName());
            }

            if (finalTuitionFee.compareTo(BigDecimal.ZERO) < 0) {
                finalTuitionFee = BigDecimal.ZERO;
            }

            FinancialTransaction tuition = new FinancialTransaction();
            tuition.setPerson(student);
            tuition.setAmount(finalTuitionFee);
            tuition.setType(TransactionType.TUITION);
            tuition.setStatus(TransactionStatus.PENDING);
            tuition.setDescription(description);
            tuition.setTransactionDate(today);
            tuition.setCreatedAt(LocalDateTime.now());

            FinancialTransaction savedTuition = financialTransactionRepository.save(tuition);
            financialTransactionNotifier.notifyStudentOfNewTransaction(savedTuition);
            log.info("Mensalidade ID {} no valor de R$ {} criada para o aluno '{}'.",
                    savedTuition.getId(), finalTuitionFee, student.getFirstName());
        }
        log.info("JOB END: Geração de mensalidades finalizada.");
    }
}