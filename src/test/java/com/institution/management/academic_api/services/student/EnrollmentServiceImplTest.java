package com.institution.management.academic_api.services.student;

import com.institution.management.academic_api.application.dto.student.CreateAttendanceRecordRequestDto;
import com.institution.management.academic_api.application.dto.student.CreateEnrollmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.EnrollmentMapper;
import com.institution.management.academic_api.application.service.student.EnrollmentServiceImpl;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.student.AttendanceRecordRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.exception.type.common.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseSectionRepository courseSectionRepository;
    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;
    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Student activeStudent;
    private CourseSection openCourseSection;
    private AcademicTerm openAcademicTerm;

    @BeforeEach
    void setUp() {
        activeStudent = new Student();
        activeStudent.setId(1L);
        activeStudent.setStatus(PersonStatus.ACTIVE);

        openAcademicTerm = new AcademicTerm();
        openAcademicTerm.setStatus(AcademicTermStatus.ENROLLMENT_OPEN);

        openCourseSection = new CourseSection();
        openCourseSection.setId(1L);
        openCourseSection.setAcademicTerm(openAcademicTerm);
    }

    @Test
    @DisplayName("enrollStudent | Deve matricular aluno com sucesso quando todas as condições são válidas")
    void enrollStudent_deveMatricularComSucesso_quandoCondicoesValidas() {
        var requestDto = new CreateEnrollmentRequestDto(1L, 1L, LocalDate.now());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(activeStudent));
        when(courseSectionRepository.findById(1L)).thenReturn(Optional.of(openCourseSection));
        when(enrollmentRepository.existsByStudentAndCourseSection(activeStudent, openCourseSection)).thenReturn(false);
        when(enrollmentMapper.toEntity(requestDto)).thenReturn(new Enrollment());

        enrollmentService.enrollStudent(requestDto);

        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("enrollStudent | Deve lançar exceção ao tentar matricular em turma que não está aberta")
    void enrollStudent_deveLancarExcecao_quandoTurmaNaoEstaAberta() {
        openAcademicTerm.setStatus(AcademicTermStatus.IN_PROGRESS); // Status inválido
        var requestDto = new CreateEnrollmentRequestDto(1L, 1L, LocalDate.now());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(activeStudent));
        when(courseSectionRepository.findById(1L)).thenReturn(Optional.of(openCourseSection));

        assertThrows(InvalidOperationException.class, () -> {
            enrollmentService.enrollStudent(requestDto);
        });

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("enrollStudent | Deve lançar exceção ao tentar matricular aluno que já está na turma")
    void enrollStudent_deveLancarExcecao_quandoAlunoJaEstaMatriculado() {
        var requestDto = new CreateEnrollmentRequestDto(1L, 1L, LocalDate.now());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(activeStudent));
        when(courseSectionRepository.findById(1L)).thenReturn(Optional.of(openCourseSection));
        when(enrollmentRepository.existsByStudentAndCourseSection(activeStudent, openCourseSection)).thenReturn(true);

        assertThrows(InvalidOperationException.class, () -> {
            enrollmentService.enrollStudent(requestDto);
        });
    }

    @Test
    @DisplayName("recordAttendance | Deve registrar falta e incrementar o total de ausências")
    void recordAttendance_deveIncrementarFaltas_quandoAlunoAusente() {
        var enrollment = new Enrollment();
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setTotalAbsences(5);

        var requestDto = new CreateAttendanceRecordRequestDto(1L, LocalDate.now(), false);
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        enrollmentService.recordAttendance(requestDto);

        ArgumentCaptor<AttendanceRecord> attendanceCaptor = ArgumentCaptor.forClass(AttendanceRecord.class);
        verify(attendanceRecordRepository).save(attendanceCaptor.capture());

        assertFalse(attendanceCaptor.getValue().getWasPresent());

        assertThat(enrollment.getTotalAbsences()).isEqualTo(6);
    }
}