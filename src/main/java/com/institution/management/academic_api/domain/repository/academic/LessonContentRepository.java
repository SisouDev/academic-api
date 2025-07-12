package com.institution.management.academic_api.domain.repository.academic;

import com.institution.management.academic_api.domain.model.entities.academic.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonContentRepository extends JpaRepository<LessonContent, Long> {
}