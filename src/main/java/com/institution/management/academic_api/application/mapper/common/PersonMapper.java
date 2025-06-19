package com.institution.management.academic_api.application.mapper.common;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.mapper.employee.EmployeeMapper;
import com.institution.management.academic_api.application.mapper.institution.InstitutionAdminMapper;
import com.institution.management.academic_api.application.mapper.institution.InstitutionMapper;
import com.institution.management.academic_api.application.mapper.student.StudentMapper;
import com.institution.management.academic_api.application.mapper.teacher.TeacherMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;


@Mapper(componentModel = "spring", uses = {
        StudentMapper.class,
        TeacherMapper.class,
        EmployeeMapper.class,
        InstitutionAdminMapper.class,
        DocumentMapper.class,
        InstitutionMapper.class
})
public interface PersonMapper {

    @SubclassMapping(source = Student.class, target = StudentResponseDto.class)
    @SubclassMapping(source = Teacher.class, target = TeacherResponseDto.class)
    @SubclassMapping(source = Employee.class, target = EmployeeDetailsDto.class)
    @SubclassMapping(source = InstitutionAdmin.class, target = InstitutionAdminResponseDto.class)
    PersonResponseDto toResponseDto(Person person);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "personType", source = "personType")
    @Mapping(target = "fullName", expression = "java(personToFullName(person))")
    PersonSummaryDto toSummaryDto(Person person);

    @Named("personToFullName")
    default String personToFullName(Person person) {
        if (person == null || person.getFirstName() == null || person.getLastName() == null) {
            return null;
        }
        return person.getFirstName() + " " + person.getLastName();
    }
}