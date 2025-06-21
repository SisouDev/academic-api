package com.institution.management.academic_api.application.mapper.wrappers.student;
import com.institution.management.academic_api.application.mapper.simple.student.StudentMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentMapperWrapper {

    @Lazy
    private final StudentMapper studentMapper;

}