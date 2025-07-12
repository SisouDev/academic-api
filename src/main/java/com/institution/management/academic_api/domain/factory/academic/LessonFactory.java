package com.institution.management.academic_api.domain.factory.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.LessonMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LessonFactory {

    private final LessonMapper lessonMapper;

    public Lesson create(CreateLessonRequestDto requestDto) {
        Lesson lesson = lessonMapper.toEntity(requestDto);
        lesson.setCreatedAt(LocalDateTime.now());
        return lesson;
    }
}