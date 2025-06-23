package com.institution.management.academic_api.services.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseMapper;
import com.institution.management.academic_api.application.service.course.CourseServiceImpl;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import jakarta.persistence.EntityExistsException;
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
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    @DisplayName("Deve criar um curso com sucesso quando os dados são válidos")
    void create_deveCriarCurso_quandoDadosValidos() {
        // Arrange (Given)
        var department = new Department();
        department.setId(1L);

        var requestDto = new CreateCourseRequestDto("Engenharia de Software", "Curso de ES", 10, 1L);
        var newCourse = new Course();
        var savedCourse = new Course();
        var responseDto = new CourseDetailsDto(1L, "Engenharia de Software", "Curso de ES", 10, null, null);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(courseRepository.existsByNameAndDepartment(requestDto.name(), department)).thenReturn(false);
        when(courseMapper.toEntity(requestDto)).thenReturn(newCourse);
        when(courseRepository.save(newCourse)).thenReturn(savedCourse);
        when(courseMapper.toDetailsDto(savedCourse)).thenReturn(responseDto);

        CourseDetailsDto result = courseService.create(requestDto);
        assertNotNull(result);
        assertEquals("Engenharia de Software", result.name());
        verify(courseRepository, times(1)).save(newCourse);
    }

    @Test
    @DisplayName("Deve lançar EntityExistsException ao tentar criar um curso duplicado")
    void create_deveLancarExcecao_quandoCursoJaExiste() {
        // Arrange
        var department = new Department();
        department.setId(1L);
        var requestDto = new CreateCourseRequestDto("Engenharia de Software", "Curso de ES", 10, 1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(courseRepository.existsByNameAndDepartment(requestDto.name(), department)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> {
            courseService.create(requestDto);
        });

        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o departamento não existe")
    void create_deveLancarExcecao_quandoDepartamentoNaoExiste() {

        var requestDto = new CreateCourseRequestDto("Engenharia de Software", "Curso de ES", 10, 99L);

        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            courseService.create(requestDto);
        });

        verify(courseRepository, never()).existsByNameAndDepartment(any(), any());
        verify(courseRepository, never()).save(any());
    }
}