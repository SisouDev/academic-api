package com.institution.management.academic_api.application.mapper.wrappers.course;
import com.institution.management.academic_api.application.mapper.simple.course.SubjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubjectMapperWrapper {

    @Lazy
    private final SubjectMapper subjectMapper;

}