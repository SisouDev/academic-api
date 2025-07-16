package com.institution.management.academic_api.domain.service.absence;

import com.institution.management.academic_api.application.dto.absence.AbsenceDetailsDto;
import com.institution.management.academic_api.application.dto.absence.CreateAbsenceRequestDto;
import com.institution.management.academic_api.application.dto.absence.ReviewAbsenceRequestDto;
import com.institution.management.academic_api.domain.model.enums.absence.AbsenceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AbsenceService {
    AbsenceDetailsDto create(CreateAbsenceRequestDto dto);
    void review(Long absenceId, ReviewAbsenceRequestDto dto, String reviewerEmail);
    String addAttachment(Long absenceId, MultipartFile file);
    AbsenceDetailsDto findById(Long id);
    List<AbsenceDetailsDto> findByPerson(Long personId);
    Page<AbsenceDetailsDto> findAll(AbsenceStatus status, Pageable pageable);

}
