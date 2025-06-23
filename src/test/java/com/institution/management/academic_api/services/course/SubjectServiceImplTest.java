package com.institution.management.academic_api.services.course;

import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.course.SubjectDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.course.SubjectMapper;
import com.institution.management.academic_api.application.service.course.SubjectServiceImpl;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.exception.type.course.CourseNotFoundException;
import com.institution.management.academic_api.exception.type.course.SubjectAlreadyExistsInCourseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    @DisplayName("Deve criar uma disciplina com sucesso quando os dados são válidos")
    void create_deveCriarDisciplina_quandoDadosValidos() {
        var course = new Course();
        course.setId(1L);

        var requestDto = new CreateSubjectRequestDto("Estrutura de Dados", 90, 1L);
        var newSubject = new Subject();
        var savedSubject = new Subject();
        var responseDto = new SubjectDetailsDto(1L, "Estrutura de Dados", 90, null, null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(subjectRepository.existsByNameAndCourse(requestDto.name(), course)).thenReturn(false);
        when(subjectMapper.toEntity(requestDto)).thenReturn(newSubject);
        when(subjectRepository.save(newSubject)).thenReturn(savedSubject);
        when(subjectMapper.toDetailsDto(savedSubject)).thenReturn(responseDto);

        SubjectDetailsDto result = subjectService.create(requestDto);

        assertNotNull(result);
        assertEquals("Estrutura de Dados", result.name());
        verify(subjectRepository, times(1)).save(newSubject);
    }

    @Test
    @DisplayName("Deve lançar SubjectAlreadyExistsInCourseException ao tentar criar uma disciplina duplicada")
    void create_deveLancarExcecao_quandoDisciplinaJaExiste() {
        var course = new Course();
        course.setId(1L);
        var requestDto = new CreateSubjectRequestDto("Estrutura de Dados", 90, 1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        when(subjectRepository.existsByNameAndCourse(requestDto.name(), course)).thenReturn(true);

        // Act & Assert
        assertThrows(SubjectAlreadyExistsInCourseException.class, () -> {
            subjectService.create(requestDto);
        });

        verify(subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar CourseNotFoundException quando o curso pai não existe")
    void create_deveLancarExcecao_quandoCursoNaoExiste() {
        var requestDto = new CreateSubjectRequestDto("Estrutura de Dados", 90, 99L);

        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            subjectService.create(requestDto);
        });

        verify(subjectRepository, never()).existsByNameAndCourse(any(), any());
        verify(subjectRepository, never()).save(any());
    }
}