package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentDefinitionRepository extends JpaRepository<AssessmentDefinition, Long> {
    List<AssessmentDefinition> findAllByCourseSectionId(Long courseSectionId);

    Optional<AssessmentDefinition> findByTitle(String s);
}