package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.*;
import com.institution.management.academic_api.application.mapper.simple.common.AddressMapper;
import com.institution.management.academic_api.application.mapper.wrappers.student.EnrollmentMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.common.Address;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, EnrollmentMapperWrapper.class})
public interface StudentMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "profilePictureUrl", source = "profilePictureUrl")
    @Mapping(target = "status", source = "status.displayName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "document", source = "document")
    @Mapping(target = "institution", source = "institution")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "enrollments", source = "enrollments")
    @Mapping(target = "formattedAddress", source = "address", qualifiedByName = "formatAddressToString")
    @Mapping(target = "generalAverage", ignore = true)
    @Mapping(target = "totalAbsences", ignore = true)
    @Mapping(target = "currentSubjects", source = "enrollments", qualifiedByName = "findCurrentSubjects")
    @Mapping(target = "courseName", source = "enrollments", qualifiedByName = "findCourseNameFromEnrollments")
    StudentResponseDto toResponseDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "document", source = "document")
    @Mapping(target = "address", source = "address")
    Student toEntity(CreateStudentRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    void updateFromDto(UpdateStudentRequestDto dto, @MappingTarget Student entity);

    @Mapping(target = "fullName", expression = "java(student.getFirstName() + \" \" + student.getLastName())")
    @Mapping(target = "status", source = "status.displayName")
    StudentSummaryDto toSummaryDto(Student student);

    @Named("formatAddressToString")
    default String formatAddressToString(Address address) {
        if (address == null) return null;
        return String.format("%s, %s - %s, %s", address.getStreet(), address.getNumber(), address.getCity(), address.getState());
    }

    @Named("findCurrentSubjects")
    default List<EnrolledSubjectDto> findCurrentSubjects(List<Enrollment> enrollments) {
        if (enrollments == null) {
            return Collections.emptyList();
        }
        return enrollments.stream()

                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .map(e -> new EnrolledSubjectDto(
                        e.getCourseSection().getId(),
                        e.getCourseSection().getSubject().getName()
                ))
                .collect(Collectors.toList());
    }

    @Named("findCourseNameFromEnrollments")
    default String findCourseNameFromEnrollments(List<Enrollment> enrollments) {
        if (enrollments == null || enrollments.isEmpty()) {
            return "Nenhum curso matriculado";
        }
        return enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .findFirst()
                .map(e -> e.getCourseSection().getSubject().getCourse().getName())
                .orElse("Nenhum curso ativo");
    }

}