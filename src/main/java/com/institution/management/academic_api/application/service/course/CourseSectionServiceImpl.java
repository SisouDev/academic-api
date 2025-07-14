package com.institution.management.academic_api.application.service.course;

import com.institution.management.academic_api.application.dto.course.CourseSectionDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.teacher.ClassListStudentDto;
import com.institution.management.academic_api.application.dto.teacher.CourseSectionDetailsForTeacherDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.application.notifiers.course.CourseSectionNotifier;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.repository.academic.AcademicTermRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.service.course.CourseSectionService;
import com.institution.management.academic_api.exception.type.academic.AcademicTermNotFoundException;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.common.InvalidOperationException;
import com.institution.management.academic_api.exception.type.course.CourseSectionAlreadyExistsInCourseException;
import com.institution.management.academic_api.exception.type.course.CourseSectionNotFoundException;
import com.institution.management.academic_api.exception.type.course.SubjectNotFoundException;
import com.institution.management.academic_api.exception.type.teacher.TeacherNotAvailableException;
import com.institution.management.academic_api.exception.type.teacher.TeacherNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseSectionServiceImpl implements CourseSectionService {

    private final AcademicTermRepository academicTermRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionMapper courseSectionMapper;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final CourseSectionNotifier courseSectionNotifier;
    private final AssessmentRepository assessmentRepository;

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
        courseSectionNotifier.notifyTeacherOfNewAssignment(savedCourseSection);
        return courseSectionMapper.toDetailsDto(savedCourseSection);

    }

    @Override
    @Transactional(readOnly = true)
    public CourseSectionDetailsForTeacherDto findDetailsForTeacher(Long sectionId) {
        CourseSection section = courseSectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + sectionId));

        checkTeacherPermission(section);
        int lessonCount = section.getLessons().size();
        int studentCount = section.getEnrollments().size();
        long pendingAssessments = 0;

        List<ClassListStudentDto> enrolledStudents = section.getEnrollments().stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.ACTIVE)
                .map(enrollment -> {
                    Student student = enrollment.getStudent();
                    BigDecimal averageGrade = assessmentRepository.findAverageScoreByEnrollment(enrollment);

                    return new ClassListStudentDto(
                            enrollment.getId(),
                            student.getId(),
                            student.getFirstName() + " " + student.getLastName(),
                            student.getEmail(),
                            averageGrade
                    );
                })
                .collect(Collectors.toList());

        return new CourseSectionDetailsForTeacherDto(
                section.getId(),
                section.getSubject().getId(),
                section.getName(),
                section.getSubject().getName(),
                lessonCount,
                studentCount,
                pendingAssessments,
                section.getLessonPlan() != null ? section.getLessonPlan().getId() : null,
                enrolledStudents
        );
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
        String oldTeacherName = courseSectionToUpdate.getTeacher().getFirstName();
        courseSectionMapper.updateFromDto(request, courseSectionToUpdate);
        CourseSection savedCourseSection = courseSectionRepository.save(courseSectionToUpdate);
        courseSectionNotifier.notifyTeacherOfUpdate(courseSectionToUpdate, oldTeacherName);
        return courseSectionMapper.toDetailsDto(savedCourseSection);
    }

    @Override
    @Transactional
    @LogActivity("Deletou uma seção de curso.")
    public void delete(Long id) {
        CourseSection courseSectionToDelete = findCourseSectionByIdOrThrow(id);
        courseSectionNotifier.notifyTeacherOfCancellation(courseSectionToDelete);
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

    private void checkTeacherPermission(CourseSection courseSection) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User teacherUser = courseSection.getTeacher().getUser();

        if (teacherUser == null || !teacherUser.getLogin().equals(username)) {
            throw new AccessDeniedException("Acesso negado. Você não é o professor desta turma.");
        }
    }
}
