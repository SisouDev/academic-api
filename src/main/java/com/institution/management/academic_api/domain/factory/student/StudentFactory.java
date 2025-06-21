package com.institution.management.academic_api.domain.factory.student;
import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.StudentMapper;
import com.institution.management.academic_api.domain.factory.common.PersonFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentFactory implements PersonFactory {

    private final StudentMapper studentMapper;

    @Override
    public Person create(Object requestDto) {
        if (!(requestDto instanceof CreateStudentRequestDto)) {
            throw new IllegalArgumentException("Invalid DTO for StudentFactory.");
        }
        return studentMapper.toEntity((CreateStudentRequestDto) requestDto);
    }

    @Override
    public PersonType supportedType() {
        return PersonType.STUDENT;
    }
}