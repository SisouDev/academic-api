package com.institution.management.academic_api.domain.factory.teacher;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.domain.factory.common.PersonFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherFactory implements PersonFactory {

    private final TeacherMapper teacherMapper;

    @Override
    public Person create(Object requestDto) {
        if (!(requestDto instanceof CreateTeacherRequestDto)) {
            throw new IllegalArgumentException("Invalid DTO for TeacherFactory.");
        }
        return teacherMapper.toEntity((CreateTeacherRequestDto) requestDto);
    }

    @Override
    public PersonType supportedType() {
        return PersonType.TEACHER;
    }
}