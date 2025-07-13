package com.institution.management.academic_api.application.service.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonDetailsDto;
import com.institution.management.academic_api.application.dto.academic.LessonSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateLessonRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.LessonMapper;
import com.institution.management.academic_api.application.notifiers.academic.LessonNotifier;
import com.institution.management.academic_api.domain.factory.academic.LessonFactory;
import com.institution.management.academic_api.domain.model.entities.academic.Lesson;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.repository.academic.LessonRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.service.academic.LessonService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final LessonFactory lessonFactory;
    private final LessonMapper lessonMapper;
    private final LessonNotifier lessonNotifier;

    @Override
    @Transactional
    @LogActivity("Criou uma nova aula.")
    public LessonDetailsDto create(CreateLessonRequestDto request) {
        CourseSection courseSection = courseSectionRepository.findById(request.courseSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Turma (CourseSection) não encontrada com ID: " + request.courseSectionId()));

        checkTeacherPermission(courseSection);

        Lesson newLesson = lessonFactory.create(request);
        newLesson.setCourseSection(courseSection);

        Lesson savedLesson = lessonRepository.save(newLesson);

        lessonNotifier.notifyStudentsOfNewLesson(savedLesson);

        return lessonMapper.toDetailsDto(savedLesson);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDetailsDto findById(Long id) {
        Lesson lesson = findLessonByIdOrThrow(id);
        return lessonMapper.toDetailsDto(lesson);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou uma aula.")
    public LessonDetailsDto update(Long id, UpdateLessonRequestDto request) {
        Lesson lessonToUpdate = findLessonByIdOrThrow(id);

        checkTeacherPermission(lessonToUpdate.getCourseSection());

        if (request.topic() != null) lessonToUpdate.setTopic(request.topic());
        if (request.description() != null) lessonToUpdate.setDescription(request.description());
        if (request.lessonDate() != null) lessonToUpdate.setLessonDate(request.lessonDate());

        Lesson updatedLesson = lessonRepository.save(lessonToUpdate);
        return lessonMapper.toDetailsDto(updatedLesson);
    }

    @Override
    @Transactional
    @LogActivity("Deletou uma aula.")
    public void delete(Long id) {
        Lesson lessonToDelete = findLessonByIdOrThrow(id);
        checkTeacherPermission(lessonToDelete.getCourseSection());
        lessonRepository.delete(lessonToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonSummaryDto> findAllBySection(Long sectionId) {
        if (!courseSectionRepository.existsById(sectionId)) {
            throw new EntityNotFoundException("Turma (CourseSection) não encontrada com ID: " + sectionId);
        }

        List<Lesson> lessons = lessonRepository.findByCourseSectionIdOrderByLessonDateDesc(sectionId);

        return lessons.stream()
                .map(lessonMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    private Lesson findLessonByIdOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aula (Lesson) não encontrada com ID: " + id));
    }

    private void checkTeacherPermission(CourseSection courseSection) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (courseSection.getTeacher() == null) {
            throw new AccessDeniedException("Acesso negado. A turma não tem um professor definido.");
        }
        User teacherUser = courseSection.getTeacher().getUser();
        if (teacherUser == null) {
            throw new AccessDeniedException("Acesso negado. O professor desta turma não tem um usuário de login.");
        }
        if (!teacherUser.getLogin().equals(username)) {
            throw new AccessDeniedException("Acesso negado. Você não é o professor desta turma.");
        }
    }
}
