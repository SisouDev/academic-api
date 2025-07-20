package com.institution.management.academic_api.application.service.humanResources;

import com.institution.management.academic_api.application.dto.employee.CreateJobHistoryRequestDto;
import com.institution.management.academic_api.application.dto.employee.JobHistoryDto;
import com.institution.management.academic_api.application.mapper.simple.employee.JobHistoryMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.SalaryStructure;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.employee.JobHistory;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.SalaryStructureRepository;
import com.institution.management.academic_api.domain.repository.helpDesk.JobHistoryRepository;
import com.institution.management.academic_api.domain.service.humanResources.HRService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HRServiceImpl implements HRService {

    private final PersonRepository personRepository;
    private final JobHistoryRepository jobHistoryRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final JobHistoryMapper jobHistoryMapper;

    @Override
    @Transactional
    public void recordJobHistoryEvent(CreateJobHistoryRequestDto dto) {
        Person person = personRepository.findById(dto.personId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada: " + dto.personId()));

        SalaryStructure newSalaryStructure = salaryStructureRepository.findById(dto.newSalaryStructureId())
                .orElseThrow(() -> new EntityNotFoundException("Estrutura salarial não encontrada: " + dto.newSalaryStructureId()));

        jobHistoryRepository.findActiveByPersonId(person.getId()).ifPresent(currentHistory -> {
            currentHistory.setEndDate(LocalDate.now());
            jobHistoryRepository.save(currentHistory);
        });

        JobHistory newHistory = new JobHistory();
        newHistory.setPerson(person);
        newHistory.setSalaryStructure(newSalaryStructure);
        newHistory.setJobPosition(newSalaryStructure.getJobPosition());
        newHistory.setStartDate(LocalDate.now());
        newHistory.setEndDate(null);
        newHistory.setDescription(dto.description());
        jobHistoryRepository.save(newHistory);

        if (person instanceof Employee employee) {
            employee.setJobPosition(newSalaryStructure.getJobPosition());
            employee.setSalaryStructure(newSalaryStructure);
            personRepository.save(employee);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobHistoryDto> findJobHistoryByPerson(Long personId) {
        return jobHistoryRepository.findByPersonIdOrderByStartDateDesc(personId)
                .stream()
                .map(jobHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobHistoryDto findJobHistoryById(Long id) {
        return jobHistoryRepository.findById(id)
                .map(jobHistoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Registro de histórico não encontrado: " + id));
    }
}