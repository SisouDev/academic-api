package com.institution.management.academic_api.application.service.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.dto.teacher.LessonPlanDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateLessonPlanRequestDto;
import com.institution.management.academic_api.application.mapper.simple.teacher.LessonPlanMapper;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.teacher.LessonPlan;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.teacher.LessonPlanRepository;
import com.institution.management.academic_api.domain.service.teacher.LessonPlanService;
import com.institution.management.academic_api.exception.type.teacher.LessonPlanAlreadyExistsException;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonPlanServiceImpl implements LessonPlanService {

    private final LessonPlanRepository lessonPlanRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final LessonPlanMapper lessonPlanMapper;

    @Override
    @Transactional
    public LessonPlanDto create(CreateLessonPlanRequestDto request) {
        CourseSection courseSection = courseSectionRepository.findById(request.courseSectionId())
                .orElseThrow(() -> new EntityNotFoundException("CourseSection not found..."));

        if (lessonPlanRepository.existsByCourseSectionId(courseSection.getId())) {
            throw new LessonPlanAlreadyExistsException("A lesson plan for this class already exists.");
        }

        LessonPlan newLessonPlan = lessonPlanMapper.toEntity(request);
        newLessonPlan.setCourseSection(courseSection);
        LessonPlan savedLessonPlan = lessonPlanRepository.save(newLessonPlan);
        return lessonPlanMapper.toDto(savedLessonPlan);
    }

    @Override
    @Transactional
    public LessonPlanDto update(Long lessonPlanId, UpdateLessonPlanRequestDto request) {
        LessonPlan lessonPlanToUpdate = lessonPlanRepository.findById(lessonPlanId)
                .orElseThrow(() -> new EntityNotFoundException("LessonPlan not found..."));

        lessonPlanMapper.updateFromDto(request, lessonPlanToUpdate);

        LessonPlan updatedLessonPlan = lessonPlanRepository.save(lessonPlanToUpdate);
        return lessonPlanMapper.toDto(updatedLessonPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonPlanDto findByCourseSection(Long courseSectionId) {
        return lessonPlanRepository.findByCourseSectionId(courseSectionId)
                .map(lessonPlanMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("LessonPlan not found for CourseSection id: " + courseSectionId));
    }

    @Override
    @Transactional
    public void delete(Long lessonPlanId) {
        if (!lessonPlanRepository.existsById(lessonPlanId)) {
            throw new EntityNotFoundException("LessonPlan not found with id: " + lessonPlanId);
        }
        lessonPlanRepository.deleteById(lessonPlanId);
    }
}