package com.institution.management.academic_api.domain.repository.academic;

import com.institution.management.academic_api.domain.model.entities.academic.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseSectionIdOrderByLessonDateDesc(Long courseSectionId);

    Page<Lesson> findAllByCourseSectionIdIn(List<Long> courseSectionIds, Pageable pageable);

}