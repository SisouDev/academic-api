package com.institution.management.academic_api.application.service.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.service.course.CourseService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.course.CourseNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo curso.")
    public CourseDetailsDto create(CreateCourseRequestDto request) {
        Department department = findDepartmentByIdOrThrow(request.departmentId());
        if (courseRepository.existsByNameAndDepartment(request.name(), department)){
            throw new EntityExistsException("Course already exists.");
        }
        Course newCourse = courseMapper.toEntity(request);
        newCourse.setDepartment(department);

        Course savedCourse = courseRepository.save(newCourse);
        return courseMapper.toDetailsDto(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailsDto findById(Long id) {
        Course course = findCourseByIdOrThrow(id);
        return courseMapper.toDetailsDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseSummaryDto> findAllByDepartment(Long departmentId) {
        Department department = findDepartmentByIdOrThrow(departmentId);
        List<Course> courses = courseRepository.findAllByDepartment(department);
        return courses.stream()
                .map(courseMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um curso.")
    public CourseDetailsDto update(Long id, UpdateCourseRequestDto request) {
        Course courseToUpdate = findCourseByIdOrThrow(id);
        courseMapper.updateFromDto(request, courseToUpdate);
        Course updatedCourse = courseRepository.save(courseToUpdate);
        return courseMapper.toDetailsDto(updatedCourse);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um curso.")
    public void delete(Long id) {
        Course courseToDelete = findCourseByIdOrThrow(id);
        courseRepository.delete(courseToDelete);
    }

    private Course findCourseByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    private Department findDepartmentByIdOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }
}
