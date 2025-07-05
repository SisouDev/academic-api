package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.AttendanceRecordRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeedingService {

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssessmentDefinitionRepository assessmentDefinitionRepository;
    private final AssessmentRepository assessmentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Transactional
    public void seedTeacherSubjectRelations(Map<String, Teacher> teachers, Map<String, Subject> subjects) {
        log.info("Iniciando seeding de Relações Professor-Matéria...");

        try {
            Teacher rossiRef = teachers.get("ROSSI");
            Subject poo = subjects.get("POO");
            Subject db = subjects.get("DB");

            if (rossiRef != null && poo != null && db != null) {
                Teacher rossi = teacherRepository.findById(rossiRef.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Seeder: Professor Rossi não encontrado."));

                rossi.getSubjects().add(poo);
                rossi.getSubjects().add(db);
                teacherRepository.save(rossi);
                log.info("Relações para o professor Rossi salvas.");
            }

            Teacher alvesRef = teachers.get("ALVES");
            Subject ml = subjects.get("ML");
            if (alvesRef != null && ml != null) {
                Teacher alves = teacherRepository.findById(alvesRef.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Seeder: Professora Alves não encontrada."));

                alves.getSubjects().add(ml);
                teacherRepository.save(alves);
                log.info("Relações para a professora Alves salvas.");
            }

        } catch (Exception e) {
            log.error("Erro durante o seeding de relações professor-matéria: {}", e.getMessage());
        }
        log.info("Seeding de Relações Professor-Matéria finalizado.");
    }

    @Transactional
    public void seedAssessmentsAndAttendance(Map<String, Enrollment> enrollments, Map<String, AssessmentDefinition> assessmentDefinitions) {
        log.info("Iniciando seeding de Notas e Frequências...");

        try {
            Enrollment enrollmentMariaRef = enrollments.get("MARIA_DB");
            AssessmentDefinition projetoFinal = assessmentDefinitions.get("Projeto Final");

            if (enrollmentMariaRef != null && projetoFinal != null) {
                Enrollment enrollmentMaria = enrollmentRepository.findById(enrollmentMariaRef.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Seeder: Matrícula da Maria não encontrada."));

                log.info("Adicionando notas e faltas para a aluna: {}", enrollmentMaria.getStudent().getFirstName());

                createAssessmentIfNotExists(enrollmentMaria, projetoFinal, new BigDecimal("9.0"));

                createAttendanceIfNotExists(enrollmentMaria, LocalDate.now().minusDays(5), false); // Uma falta

                long totalAbsences = attendanceRecordRepository.countByEnrollmentAndWasPresent(enrollmentMaria, false);
                enrollmentMaria.setTotalAbsences((int) totalAbsences);
            }
        } catch (Exception e) {
            log.error("Erro durante o seeding de notas e frequência: {}", e.getMessage(), e);
        }

        log.info("Seeding de Notas e Frequências finalizado.");
    }

    private void createAssessmentIfNotExists(Enrollment enrollment, AssessmentDefinition definition, BigDecimal score) {
        if (assessmentRepository.findByEnrollmentAndAssessmentDefinition(enrollment, definition).isEmpty()) {
            Assessment assessment = new Assessment();
            assessment.setEnrollment(enrollment);
            assessment.setAssessmentDefinition(definition);
            assessment.setScore(score);
            assessment.setAssessmentDate(definition.getAssessmentDate() != null ? definition.getAssessmentDate() : LocalDate.now());
            assessment.setType(definition.getType());
            assessmentRepository.save(assessment);
        }
    }

    private void createAttendanceIfNotExists(Enrollment enrollment, LocalDate date, boolean wasPresent) {
        if (attendanceRecordRepository.findByEnrollmentAndDate(enrollment, date).isEmpty()) {
            AttendanceRecord record = new AttendanceRecord();
            record.setEnrollment(enrollment);
            record.setDate(date);
            record.setWasPresent(wasPresent);
            attendanceRecordRepository.save(record);
        }
    }
}