package com.institution.management.academic_api.services.course;

import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.application.service.course.CourseSectionServiceImpl;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.repository.academic.AcademicTermRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.exception.type.common.InvalidOperationException;
import com.institution.management.academic_api.exception.type.course.CourseSectionAlreadyExistsInCourseException;
import com.institution.management.academic_api.exception.type.teacher.TeacherNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseSectionServiceImplTest {

    @Mock
    private CourseSectionRepository courseSectionRepository;
    @Mock
    private AcademicTermRepository academicTermRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private CourseSectionMapper courseSectionMapper;

    @InjectMocks
    private CourseSectionServiceImpl courseSectionService;

    private AcademicTerm validTerm;
    private Subject validSubject;
    private Teacher activeTeacher;
    private CreateCourseSectionRequestDto requestDto;

    @BeforeEach
    void setUp() {
        validTerm = new AcademicTerm();
        validTerm.setId(1L);
        validTerm.setStatus(AcademicTermStatus.ENROLLMENT_OPEN);

        validSubject = new Subject();
        validSubject.setId(1L);

        activeTeacher = new Teacher();
        activeTeacher.setId(1L);
        activeTeacher.setStatus(PersonStatus.ACTIVE);

        requestDto = new CreateCourseSectionRequestDto("Turma A", "Sala 101", 1L, 1L, 1L);
    }

    @Test
    @DisplayName("Deve criar uma turma com sucesso quando todos os dados e status são válidos")
    void create_deveCriarTurma_quandoDadosSaoValidos() {
        when(academicTermRepository.findById(1L)).thenReturn(Optional.of(validTerm));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(validSubject));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(activeTeacher));
        when(courseSectionRepository.existsCourseSectionByNameAndAcademicTerm("Turma A", validTerm)).thenReturn(false);
        when(courseSectionMapper.toEntity(requestDto)).thenReturn(new CourseSection());
        when(courseSectionRepository.save(any(CourseSection.class))).thenReturn(new CourseSection());

        courseSectionService.create(requestDto);

        verify(courseSectionRepository, times(1)).save(any(CourseSection.class));
    }

    @Test
    @DisplayName("Deve lançar InvalidOperationException se o período letivo não estiver aberto para matrículas")
    void create_deveLancarExcecao_quandoPeriodoLetivoNaoEstaAberto() {
        validTerm.setStatus(AcademicTermStatus.COMPLETED);
        when(academicTermRepository.findById(1L)).thenReturn(Optional.of(validTerm));

        assertThrows(InvalidOperationException.class, () -> {
            courseSectionService.create(requestDto);
        });

        verify(courseSectionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar TeacherNotAvailableException se o professor não estiver ativo")
    void create_deveLancarExcecao_quandoProfessorNaoEstaAtivo() {
        activeTeacher.setStatus(PersonStatus.INACTIVE);
        when(academicTermRepository.findById(1L)).thenReturn(Optional.of(validTerm));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(validSubject));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(activeTeacher));

        assertThrows(TeacherNotAvailableException.class, () -> {
            courseSectionService.create(requestDto);
        });

        verify(courseSectionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar CourseSectionAlreadyExistsInCourseException se a turma já existir no período")
    void create_deveLancarExcecao_quandoTurmaJaExiste() {
        when(academicTermRepository.findById(1L)).thenReturn(Optional.of(validTerm));
        when(courseSectionRepository.existsCourseSectionByNameAndAcademicTerm("Turma A", validTerm)).thenReturn(true); // Turma já existe!

        assertThrows(CourseSectionAlreadyExistsInCourseException.class, () -> {
            courseSectionService.create(requestDto);
        });

        verify(courseSectionRepository, never()).save(any());
    }
}