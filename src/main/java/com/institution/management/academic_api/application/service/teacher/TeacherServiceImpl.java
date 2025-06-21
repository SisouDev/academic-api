package com.institution.management.academic_api.application.service.teacher;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateTeacherRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.service.teacher.TeacherService;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final InstitutionRepository institutionRepository;
    private final UserService userService;
    private final PersonMapper personMapper;

    @Override
    public TeacherResponseDto create(CreateTeacherRequestDto request) {
        return null;
    }

    @Override
    public TeacherResponseDto findById(Long id) {
        return null;
    }

    @Override
    public List<PersonSummaryDto> findAllByInstitution(Long institutionId) {
        return List.of();
    }

    @Override
    public TeacherResponseDto update(Long id, UpdateTeacherRequestDto request) {
        return null;
    }

    private Institution findInstitutionByIdOrThrow(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution not found with id: " + id));
    }
}
