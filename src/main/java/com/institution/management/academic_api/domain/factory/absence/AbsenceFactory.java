package com.institution.management.academic_api.domain.factory.absence;

import com.institution.management.academic_api.application.dto.absence.CreateAbsenceRequestDto;
import com.institution.management.academic_api.application.mapper.simple.absence.AbsenceMapper;
import com.institution.management.academic_api.domain.model.entities.absence.Absence;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.absence.AbsenceStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AbsenceFactory {

    private final AbsenceMapper absenceMapper;
    private final PersonRepository personRepository;

    public Absence create(CreateAbsenceRequestDto dto) {
        Absence absence = absenceMapper.toEntity(dto);

        Person person = personRepository.findById(dto.personId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + dto.personId()));
        absence.setPerson(person);

        absence.setCreatedAt(LocalDateTime.now());
        absence.setStatus(AbsenceStatus.PENDING_REVIEW);

        return absence;
    }
}