package com.institution.management.academic_api.infra.config;

import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.student.AssessmentType;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.AttendanceRecordRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
@Order(5)
public class DataSeederV5 implements CommandLineRunner {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentDefinitionRepository assessmentDefinitionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final CourseSectionRepository courseSectionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Executando DataSeederV5 (Versão Final)...");

        if (assessmentRepository.count() > 10) {
            log.info("DataSeederV5 parece já ter sido executado. Pulando execução.");
            return;
        }

        seedAdditionalAcademicData();

        log.info("DataSeederV5 (Versão Final) finalizado com sucesso!");
    }

    private void seedAdditionalAcademicData() {
        List<CourseSection> allSections = courseSectionRepository.findAll();

        for (CourseSection section : allSections) {
            if (section.getName().contains("POO_A")) {

                AssessmentDefinition p1 = createAssessmentDefinition(section, "Prova 1", AssessmentType.EXAM, new BigDecimal("10.0"));

                for (Enrollment enrollment : section.getEnrollments()) {

                    BigDecimal randomGrade = BigDecimal.valueOf(5 + (Math.random() * 5)).setScale(1, BigDecimal.ROUND_HALF_UP);
                    createAssessment(enrollment, p1, randomGrade);

                    createAttendanceRecord(enrollment, LocalDate.now().minusDays(20), true);
                    createAttendanceRecord(enrollment, LocalDate.now().minusDays(19), false);
                }
            }
        }
    }

    private AssessmentDefinition createAssessmentDefinition(CourseSection section, String title, AssessmentType type, BigDecimal weight) {
        AssessmentDefinition newDefinition = new AssessmentDefinition();
        newDefinition.setCourseSection(section);
        newDefinition.setTitle(title);
        newDefinition.setType(type);
        newDefinition.setAssessmentDate(LocalDate.now().plusMonths(1));
        newDefinition.setWeight(weight);
        return assessmentDefinitionRepository.save(newDefinition);
    }

    private void createAssessment(Enrollment enrollment, AssessmentDefinition definition, BigDecimal score) {
        boolean assessmentExists = enrollment.getAssessments().stream()
                .anyMatch(a -> a.getAssessmentDefinition().getId().equals(definition.getId()));

        if (!assessmentExists) {
            Assessment newAssessment = new Assessment();
            newAssessment.setEnrollment(enrollment);
            newAssessment.setAssessmentDefinition(definition);
            newAssessment.setScore(score);
            newAssessment.setAssessmentDate(LocalDate.now());
            newAssessment.setType(definition.getType());
            assessmentRepository.save(newAssessment);
            log.info("Nota {} lançada para '{}' do aluno {}", score, definition.getTitle(), enrollment.getStudent().getFirstName());
        }
    }

    private void createAttendanceRecord(Enrollment enrollment, LocalDate date, boolean wasPresent) {
        boolean attendanceExists = enrollment.getAttendanceRecords().stream()
                .anyMatch(ar -> ar.getDate().equals(date));

        if (!attendanceExists) {
            AttendanceRecord newAttendance = new AttendanceRecord();
            newAttendance.setEnrollment(enrollment);
            newAttendance.setDate(date);
            newAttendance.setWasPresent(wasPresent);
            attendanceRecordRepository.save(newAttendance);
        }
    }
}