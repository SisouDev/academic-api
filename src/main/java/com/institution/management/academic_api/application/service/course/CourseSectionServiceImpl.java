package com.institution.management.academic_api.application.service.course;

import com.institution.management.academic_api.application.dto.course.CourseSectionDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseSectionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.repository.academic.AcademicTermRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.service.course.CourseSectionService;
import com.institution.management.academic_api.exception.type.academic.AcademicTermNotFoundException;
import com.institution.management.academic_api.exception.type.common.InvalidOperationException;
import com.institution.management.academic_api.exception.type.course.CourseSectionAlreadyExistsInCourseException;
import com.institution.management.academic_api.exception.type.course.CourseSectionNotFoundException;
import com.institution.management.academic_api.exception.type.course.SubjectNotFoundException;
import com.institution.management.academic_api.exception.type.teacher.TeacherNotAvailableException;
import com.institution.management.academic_api.exception.type.teacher.TeacherNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSectionServiceImpl implements CourseSectionService {

    private final AcademicTermRepository academicTermRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionMapper courseSectionMapper;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    @LogActivity("Cadastrou uma nova seção de curso.")
    public CourseSectionDetailsDto create(CreateCourseSectionRequestDto request) {
        AcademicTerm academicTerm = findAcademicTermByIdOrThrow(request.academicTermId());
        if (academicTerm.getStatus() != AcademicTermStatus.PLANNING && academicTerm.getStatus() != AcademicTermStatus.ENROLLMENT_OPEN) {
            throw new InvalidOperationException("It is not possible to create classes for a school term that is not in planning or open for enrollment.");
        }
        if (courseSectionRepository.existsCourseSectionByNameAndAcademicTerm(request.name(), academicTerm)){
            throw new CourseSectionAlreadyExistsInCourseException("Course section already exists in Academic Term: " + request.name() + " " + request.academicTermId());
        }
        Subject subject = findCourseSubjectByIdOrThrow(request.subjectId());
        Teacher teacher = findTeacherTermByIdOrThrow(request.teacherId());
        if (teacher.getStatus() != PersonStatus.ACTIVE){
            throw new TeacherNotAvailableException("Unable to assign an inactive/blocked teacher to the class.");
        }
        CourseSection newCourseSection = courseSectionMapper.toEntity(request);
        newCourseSection.setAcademicTerm(academicTerm);
        newCourseSection.setSubject(subject);
        newCourseSection.setTeacher(teacher);
        CourseSection savedCourseSection = courseSectionRepository.save(newCourseSection);
        return courseSectionMapper.toDetailsDto(savedCourseSection);

    }

    @Override
    @Transactional(readOnly = true)
    public CourseSectionDetailsDto findById(Long id) {
        CourseSection courseSection = findCourseSectionByIdOrThrow(id);
        return courseSectionMapper.toDetailsDto(courseSection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseSectionSummaryDto> findAllByAcademicTerm(Long academicTermId) {
        AcademicTerm academicTerm = findAcademicTermByIdOrThrow(academicTermId);
        List<CourseSection> courseSections = courseSectionRepository.findAllByAcademicTerm(academicTerm);
        return courseSections.stream()
                .map(courseSectionMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    @LogActivity("Atualizou uma seção de curso.")
    public CourseSectionDetailsDto update(Long id, UpdateCourseSectionRequestDto request) {
        CourseSection courseSectionToUpdate = findCourseSectionByIdOrThrow(id);
        courseSectionMapper.updateFromDto(request, courseSectionToUpdate);
        CourseSection savedCourseSection = courseSectionRepository.save(courseSectionToUpdate);
        return courseSectionMapper.toDetailsDto(savedCourseSection);
    }

    @Override
    @Transactional
    @LogActivity("Deletou uma seção de curso.")
    public void delete(Long id) {
        CourseSection courseSectionToDelete = findCourseSectionByIdOrThrow(id);
        courseSectionRepository.delete(courseSectionToDelete);
    }

    private CourseSection findCourseSectionByIdOrThrow(Long id) {
        return courseSectionRepository.findById(id)
                .orElseThrow(() -> new CourseSectionNotFoundException("Course Section not found with id: " + id));
    }

    private Subject findCourseSubjectByIdOrThrow(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + id));
    }

    private AcademicTerm findAcademicTermByIdOrThrow(Long id) {
        return academicTermRepository.findById(id)
                .orElseThrow(() -> new AcademicTermNotFoundException("Academic Term not found with id: " + id));
    }

    private Teacher findTeacherTermByIdOrThrow(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));
    }
}
