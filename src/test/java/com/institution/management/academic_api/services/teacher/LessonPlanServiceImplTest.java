package com.institution.management.academic_api.services.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.dto.teacher.LessonPlanDto;
import com.institution.management.academic_api.application.mapper.simple.teacher.LessonPlanMapper;
import com.institution.management.academic_api.application.service.teacher.LessonPlanServiceImpl;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.teacher.LessonPlan;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.teacher.LessonPlanRepository;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.teacher.LessonPlanAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonPlanServiceImplTest {

    @Mock
    private LessonPlanRepository lessonPlanRepository;
    @Mock
    private CourseSectionRepository courseSectionRepository;
    @Mock
    private LessonPlanMapper lessonPlanMapper;

    @InjectMocks
    private LessonPlanServiceImpl lessonPlanService;

    private CreateLessonPlanRequestDto requestDto;
    private CourseSection courseSection;

    @BeforeEach
    void setUp() {
        courseSection = new CourseSection();
        courseSection.setId(1L);

        requestDto = new CreateLessonPlanRequestDto(1L, "Objectives", "Syllabus", "Bibliography");
    }

    @Test
    @DisplayName("Deve criar um plano de aula com sucesso quando a turma não possui um")
    void create_deveCriarPlanoDeAula_quandoTurmaNaoTemPlano() {
        when(courseSectionRepository.findById(1L)).thenReturn(Optional.of(courseSection));
        when(lessonPlanRepository.existsByCourseSectionId(1L)).thenReturn(false);
        when(lessonPlanMapper.toEntity(requestDto)).thenReturn(new LessonPlan());
        when(lessonPlanRepository.save(any(LessonPlan.class))).thenReturn(new LessonPlan());
        when(lessonPlanMapper.toDto(any(LessonPlan.class))).thenReturn(new LessonPlanDto(1L, null, null, null, null));

        LessonPlanDto result = lessonPlanService.create(requestDto);

        assertNotNull(result);
        verify(lessonPlanRepository, times(1)).save(any(LessonPlan.class));
    }

    @Test
    @DisplayName("Deve lançar LessonPlanAlreadyExistsException ao tentar criar plano para turma que já possui um")
    void create_deveLancarExcecao_quandoPlanoDeAulaJaExiste() {
        when(courseSectionRepository.findById(1L)).thenReturn(Optional.of(courseSection));
        when(lessonPlanRepository.existsByCourseSectionId(1L)).thenReturn(true);

        assertThrows(LessonPlanAlreadyExistsException.class, () -> {
            lessonPlanService.create(requestDto);
        });

        verify(lessonPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando a turma (CourseSection) não existe")
    void create_deveLancarExcecao_quandoTurmaNaoExiste() {
        when(courseSectionRepository.findById(99L)).thenReturn(Optional.empty());
        var requestComIdInexistente = new CreateLessonPlanRequestDto(99L, "Obj", "Syllabus", "Bib");

        assertThrows(EntityNotFoundException.class, () -> {
            lessonPlanService.create(requestComIdInexistente);
        });

        verify(lessonPlanRepository, never()).save(any());
    }
}