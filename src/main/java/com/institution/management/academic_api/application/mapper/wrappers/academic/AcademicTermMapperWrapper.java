package com.institution.management.academic_api.application.mapper.wrappers.academic;
import com.institution.management.academic_api.application.mapper.simple.academic.AcademicTermMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcademicTermMapperWrapper {

    @Lazy
    private final AcademicTermMapper academicTermMapper;

}