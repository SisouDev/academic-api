package com.institution.management.academic_api.application.mapper.simple.institution;

import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.simple.common.AddressMapper;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Address;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        AddressMapper.class,
        DepartmentMapper.class,
        PersonMapper.class
})
public interface InstitutionMapper {

    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    InstitutionSummaryDto toSummaryDto(Institution institution);

    @Mapping(target = "membersCount", expression = "java(institution.getMembers() != null ? institution.getMembers().size() : 0)")
    @Mapping(target = "studentCount", source = "members", qualifiedByName = "countStudents")
    @Mapping(target = "teacherCount", source = "members", qualifiedByName = "countTeachers")
    @Mapping(target = "courseCount", source = "departments", qualifiedByName = "countCourses")
    @Mapping(target = "formattedAddress", source = "address", qualifiedByName = "formatAddressToString")
    InstitutionDetailsDto toDetailsDto(Institution institution);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "academicTerms", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "admins", ignore = true)
    @Mapping(target = "address.street", source = "address.street")
    @Mapping(target = "address.number", source = "address.number")
    Institution toEntity(CreateInstitutionRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "academicTerms", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "admins", ignore = true)
    void updateFromDto(UpdateInstitutionRequestDto dto, @MappingTarget Institution entity);

    @Named("countStudents")
    default long countStudents(List<Person> members) {
        if (members == null) return 0;
        return members.stream().filter(Student.class::isInstance).count();
    }

    @Named("countTeachers")
    default long countTeachers(List<Person> members) {
        if (members == null) return 0;
        return members.stream().filter(Teacher.class::isInstance).count();
    }

    @Named("countCourses")
    default long countCourses(List<Department> departments) {
        if (departments == null) return 0;
        return departments.stream()
                .mapToLong(department -> department.getCourses() != null ? department.getCourses().size() : 0)
                .sum();
    }

    @Named("formatAddressToString")
    default String formatAddressToString(Address address) {
        if (address == null) return null;
        return String.format("%s, %s - %s, %s", address.getStreet(), address.getNumber(), address.getCity(), address.getState());
    }

}