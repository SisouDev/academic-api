package com.institution.management.academic_api.services.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.AssessmentMapper;
import com.institution.management.academic_api.application.service.student.AssessmentServiceImpl;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.exception.type.student.AssessmentNotFoundException;
import com.institution.management.academic_api.exception.type.student.EnrollmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private AssessmentMapper assessmentMapper;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    private CreateAssessmentRequestDto requestDto;
    private Enrollment activeEnrollment;

    @BeforeEach
    void setUp() {
        requestDto = new CreateAssessmentRequestDto(1L, new BigDecimal("8.5"), LocalDate.now(), "EXAM");

        activeEnrollment = new Enrollment();
        activeEnrollment.setId(1L);
        activeEnrollment.setStatus(EnrollmentStatus.ACTIVE);
    }

    @Test
    @DisplayName("Deve adicionar uma avaliação com sucesso quando a matrícula está ativa")
    void addAssessmentToEnrollment_deveAdicionarAvaliacao_quandoMatriculaAtiva() {
        var newAssessment = new Assessment();
        var savedAssessment = new Assessment();
        var responseDto = new AssessmentDto(1L, new BigDecimal("8.5"), LocalDate.now(), "EXAM");

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(activeEnrollment));
        when(assessmentMapper.toEntity(requestDto)).thenReturn(newAssessment);
        when(assessmentRepository.save(newAssessment)).thenReturn(savedAssessment);
        when(assessmentMapper.toDto(savedAssessment)).thenReturn(responseDto);

        AssessmentDto result = assessmentService.addAssessmentToEnrollment(requestDto);
        assertNotNull(result);
        assertEquals(new BigDecimal("8.5"), result.score());
        verify(assessmentRepository, times(1)).save(newAssessment);
    }

    @Test
    @DisplayName("Deve lançar InvalidOperationException ao adicionar avaliação em matrícula não ativa")
    void addAssessmentToEnrollment_deveLancarExcecao_quandoMatriculaNaoEstaAtiva() {
        var completedEnrollment = new Enrollment();
        completedEnrollment.setId(1L);
        completedEnrollment.setStatus(EnrollmentStatus.COMPLETED);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(completedEnrollment));

        assertThrows(NullPointerException.class, () -> {
            assessmentService.addAssessmentToEnrollment(requestDto);
        });

        verify(assessmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar EnrollmentNotFoundException quando a matrícula não existe")
    void addAssessmentToEnrollment_deveLancarExcecao_quandoMatriculaNaoExiste() {
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());
        var requestDtoComIdInexistente = new CreateAssessmentRequestDto(99L, new BigDecimal("8.5"), LocalDate.now(), "EXAM");

        assertThrows(EnrollmentNotFoundException.class, () -> {
            assessmentService.addAssessmentToEnrollment(requestDtoComIdInexistente);
        });
    }

    @Test
    @DisplayName("Deve deletar uma avaliação com sucesso")
    void deleteAssessment_deveChamarDeleteDoRepositorio_quandoIdExiste() {
        var assessmentToDelete = new Assessment();
        assessmentToDelete.setId(1L);
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessmentToDelete));
        doNothing().when(assessmentRepository).delete(assessmentToDelete);

        assessmentService.deleteAssessment(1L);

        verify(assessmentRepository, times(1)).delete(assessmentToDelete);
    }

    @Test
    @DisplayName("Deve lançar AssessmentNotFoundException ao tentar deletar avaliação inexistente")
    void deleteAssessment_deveLancarExcecao_quandoIdNaoExiste() {
        when(assessmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.deleteAssessment(99L);
        });
    }
}