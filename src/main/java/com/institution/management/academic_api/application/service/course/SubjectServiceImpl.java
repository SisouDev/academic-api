package com.institution.management.academic_api.application.service.course;

import com.institution.management.academic_api.application.dto.academic.LessonDetailsDto;
import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.course.SubjectDetailsDto;
import com.institution.management.academic_api.application.dto.course.SubjectSummaryDto;
import com.institution.management.academic_api.application.dto.course.UpdateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentSubjectDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.academic.LessonMapper;
import com.institution.management.academic_api.application.mapper.simple.course.SubjectMapper;
import com.institution.management.academic_api.application.notifiers.course.SubjectNotifier;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.repository.academic.LessonRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.service.course.SubjectService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.course.CourseNotFoundException;
import com.institution.management.academic_api.exception.type.course.SubjectAlreadyExistsInCourseException;
import com.institution.management.academic_api.exception.type.course.SubjectNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final CourseRepository courseRepository;
    private final SubjectNotifier subjectNotifier;
    private final CourseSectionRepository courseSectionRepository;
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo tópico para o curso.")
    public SubjectDetailsDto create(CreateSubjectRequestDto request) {
        Course course = findCourseByIdOrThrow(request.courseId());
        if (subjectRepository.existsByNameAndCourse(request.name(), course)){
            throw new SubjectAlreadyExistsInCourseException("Subject already exists.");
        }
        Subject newSubject = subjectMapper.toEntity(request);
        newSubject.setCourse(course);
        Subject savedSubject = subjectRepository.save(newSubject);
        subjectNotifier.notifyAdminOfNewSubject(savedSubject);
        return subjectMapper.toDetailsDto(savedSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDetailsDto findById(Long id) {
        Subject subject = findSubjectByIdOrThrow(id);
        return subjectMapper.toDetailsDto(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectSummaryDto> findAllByCourse(Long courseId) {
        Course course = findCourseByIdOrThrow(courseId);
        List<Subject> subjects = subjectRepository.findAllByCourse(course);
        return subjects.stream()
                .map(subjectMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um tópico para o curso.")
    public SubjectDetailsDto update(Long id, UpdateSubjectRequestDto request) {
        Subject subjectToUpdate = findSubjectByIdOrThrow(id);
        subjectMapper.updateFromDto(request, subjectToUpdate);
        Subject updatedSubject = subjectRepository.save(subjectToUpdate);
        subjectNotifier.notifyAdminOfSubjectUpdate(subjectToUpdate);
        return subjectMapper.toDetailsDto(updatedSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentSubjectDetailsDto findSubjectDetailsForPage(Long subjectId) {
        List<AcademicTermStatus> activeStatuses = List.of(
                AcademicTermStatus.IN_PROGRESS,
                AcademicTermStatus.ENROLLMENT_OPEN
        );
        CourseSection section = courseSectionRepository
                .findTopBySubjectIdAndAcademicTerm_StatusIn(subjectId, activeStatuses)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma turma ativa encontrada para a matéria com ID: " + subjectId));

        Teacher teacher = section.getTeacher();

        List<LessonDetailsDto> lessons = lessonRepository.findByCourseSectionIdOrderByLessonDateDesc(section.getId())
                .stream()
                .map(lessonMapper::toDetailsDto)
                .collect(Collectors.toList());

        return new StudentSubjectDetailsDto(
                section.getSubject().getId(),
                section.getId(), // courseSectionId
                section.getSubject().getName(),
                section.getName(),
                teacher.getFirstName() + " " + teacher.getLastName(),
                teacher.getEmail(),
                lessons
        );
    }

    @Override
    @Transactional
    @LogActivity("Deletou um tópico para o curso.")
    public void delete(Long id) {
        Subject subjectToDelete = findSubjectByIdOrThrow(id);
        subjectNotifier.notifyAdminOfSubjectDeletion(subjectToDelete);
        subjectRepository.delete(subjectToDelete);
    }

    private Subject findSubjectByIdOrThrow(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + id));
    }

    private Course findCourseByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }
}
