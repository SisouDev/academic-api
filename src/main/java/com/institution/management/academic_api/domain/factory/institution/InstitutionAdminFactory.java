package com.institution.management.academic_api.domain.factory.institution;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionAdminMapper;
import com.institution.management.academic_api.domain.factory.common.PersonFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstitutionAdminFactory implements PersonFactory {

    private final InstitutionAdminMapper institutionAdminMapper;

    @Override
    public Person create(Object requestDto) {
        if (!(requestDto instanceof CreateInstitutionAdminRequestDto)) {
            throw new IllegalArgumentException("Invalid DTO for InstitutionAdminFactory.");
        }
        return institutionAdminMapper.toEntity((CreateInstitutionAdminRequestDto) requestDto);
    }

    @Override
    public PersonType supportedType() {
        return PersonType.STAFF;
    }
}