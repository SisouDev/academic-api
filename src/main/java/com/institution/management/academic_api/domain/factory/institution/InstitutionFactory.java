package com.institution.management.academic_api.domain.factory.institution;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.mapper.institution.InstitutionMapper;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InstitutionFactory {

    private final InstitutionMapper institutionMapper;

    public Institution create(CreateInstitutionRequestDto requestDto) {
        Institution institution = institutionMapper.toEntity(requestDto);
        institution.setCreatedAt(LocalDateTime.now());
        return institution;
    }
}