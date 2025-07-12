package com.institution.management.academic_api.application.service.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonContentRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonContentDto;
import com.institution.management.academic_api.application.dto.academic.UpdateLessonContentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.LessonContentMapper;
import com.institution.management.academic_api.application.notifiers.academic.LessonContentNotifier;
import com.institution.management.academic_api.domain.factory.academic.LessonContentFactory;
import com.institution.management.academic_api.domain.model.entities.academic.Lesson;
import com.institution.management.academic_api.domain.model.entities.academic.LessonContent;
import com.institution.management.academic_api.domain.model.enums.academic.LessonContentType;
import com.institution.management.academic_api.domain.repository.academic.LessonContentRepository;
import com.institution.management.academic_api.domain.repository.academic.LessonRepository;
import com.institution.management.academic_api.domain.service.academic.LessonContentService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import com.institution.management.academic_api.infra.utils.HtmlSanitizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonContentServiceImpl implements LessonContentService {
    private final LessonContentRepository lessonContentRepository;
    private final LessonRepository lessonRepository;
    private final LessonContentFactory lessonContentFactory;
    private final LessonContentMapper lessonContentMapper;
    private final LessonContentNotifier lessonContentNotifier;
    private final HtmlSanitizerService htmlSanitizerService;


    @Override
    @Transactional
    @LogActivity("Adicionou conteúdo a uma aula.")
    public LessonContentDto create(CreateLessonContentRequestDto request) {
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new EntityNotFoundException("Aula (Lesson) não encontrada com ID: " + request.lessonId()));

        checkTeacherPermission(lesson);

        LessonContent newContent = lessonContentFactory.create(request);

        String safeContent = htmlSanitizerService.sanitize(request.content());
        newContent.setContent(safeContent);

        newContent.setLesson(lesson);
        LessonContent savedContent = lessonContentRepository.save(newContent);

        lessonContentNotifier.notifyStudentsOfNewContent(savedContent);
        return lessonContentMapper.toDto(savedContent);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um conteúdo de aula.")
    public LessonContentDto update(Long id, UpdateLessonContentRequestDto request) {
        LessonContent contentToUpdate = findLessonContentByIdOrThrow(id);
        checkTeacherPermission(contentToUpdate.getLesson());

        if (request.type() != null) {
            contentToUpdate.setType(LessonContentType.valueOf(request.type().toUpperCase()));
        }
        if (request.content() != null) {
            String safeContent = htmlSanitizerService.sanitize(request.content());
            contentToUpdate.setContent(safeContent);
        }
        if (request.displayOrder() != 0) {
            contentToUpdate.setDisplayOrder(request.displayOrder());
        }

        LessonContent updatedContent = lessonContentRepository.save(contentToUpdate);
        return lessonContentMapper.toDto(updatedContent);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um conteúdo de aula.")
    public void delete(Long id) {
        LessonContent contentToDelete = findLessonContentByIdOrThrow(id);
        checkTeacherPermission(contentToDelete.getLesson());

        lessonContentRepository.delete(contentToDelete);
    }

    private LessonContent findLessonContentByIdOrThrow(Long id) {
        return lessonContentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo de Aula (LessonContent) não encontrado com ID: " + id));
    }

    private void checkTeacherPermission(Lesson lesson) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!lesson.getCourseSection().getTeacher().getUser().getLogin().equals(username)) {
            throw new AccessDeniedException("Acesso negado. Você não é o professor desta turma.");
        }
    }
}
