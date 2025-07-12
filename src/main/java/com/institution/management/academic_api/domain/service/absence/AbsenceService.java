package com.institution.management.academic_api.domain.service.absence;

import com.institution.management.academic_api.application.dto.absence.AbsenceDetailsDto;
import com.institution.management.academic_api.application.dto.absence.CreateAbsenceRequestDto;
import com.institution.management.academic_api.application.dto.absence.ReviewAbsenceRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AbsenceService {
    AbsenceDetailsDto create(CreateAbsenceRequestDto dto);
    void review(Long absenceId, ReviewAbsenceRequestDto dto, String reviewerEmail);
    String addAttachment(Long absenceId, MultipartFile file);
    AbsenceDetailsDto findById(Long id);
    List<AbsenceDetailsDto> findByPerson(Long personId);

}
