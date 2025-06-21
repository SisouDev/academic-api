package com.institution.management.academic_api.application.mapper.wrappers.common;
import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonMapperWrapper {

    @Lazy
    private final PersonMapper personMapper;

    @Named("personToFullName")
    public String personToFullName(Person person) {
        return personMapper.personToFullName(person);
    }

    public PersonSummaryDto toSummaryDto(Person person) {
        return personMapper.toSummaryDto(person);
    }

    public PersonResponseDto toResponseDto(Person person) {
        return personMapper.toResponseDto(person);
    }
}