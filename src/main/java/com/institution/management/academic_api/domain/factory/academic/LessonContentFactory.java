package com.institution.management.academic_api.domain.factory.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonContentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.LessonContentMapper;
import com.institution.management.academic_api.domain.model.entities.academic.LessonContent;
import com.institution.management.academic_api.domain.model.enums.academic.LessonContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonContentFactory {

    private final LessonContentMapper lessonContentMapper;

    public LessonContent create(CreateLessonContentRequestDto requestDto) {
        LessonContent content = lessonContentMapper.toEntity(requestDto);

        LessonContentType type = LessonContentType.valueOf(requestDto.type().toUpperCase());
        content.setType(type);

        return content;
    }
}