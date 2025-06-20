package com.institution.management.academic_api.domain.factory.academic;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.mapper.academic.AcademicTermMapper;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcademicTermFactory {

    private final AcademicTermMapper academicTermMapper;

    public AcademicTerm create(AcademicTermRequestDto requestDto) {
        AcademicTerm academicTerm = academicTermMapper.toEntity(requestDto);

        academicTerm.setStatus(AcademicTermStatus.PLANNING);

        return academicTerm;
    }
}