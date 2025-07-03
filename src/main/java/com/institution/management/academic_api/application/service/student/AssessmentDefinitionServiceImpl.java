package com.institution.management.academic_api.application.service.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDefinitionDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.AssessmentDefinitionMapper;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.service.student.AssessmentDefinitionService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentDefinitionServiceImpl implements AssessmentDefinitionService {
    private final AssessmentDefinitionRepository repository;
    private final CourseSectionRepository courseSectionRepository;
    private final AssessmentDefinitionMapper mapper;

    @Override
    @Transactional
    public AssessmentDefinitionDto create(CreateAssessmentDefinitionRequestDto request) {
        CourseSection courseSection = courseSectionRepository.findById(request.courseSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Class not found."));

        AssessmentDefinition newDef = mapper.toEntity(request);
        newDef.setCourseSection(courseSection);

        return mapper.toDto(repository.save(newDef));
    }

    @Override
    public List<AssessmentDefinitionDto> findByCourseSection(Long courseSectionId) {
        return repository.findAllByCourseSectionId(courseSectionId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssessmentDefinitionDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Assessment definition not found."));
    }

    @Override
    @Transactional
    public AssessmentDefinitionDto update(Long id, UpdateAssessmentDefinitionRequestDto request) {
        AssessmentDefinition definition = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment definition not found."));
        mapper.updateFromDto(request, definition);
        return mapper.toDto(repository.save(definition));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Assessment definition not found.");
        }
        repository.deleteById(id);
    }
}