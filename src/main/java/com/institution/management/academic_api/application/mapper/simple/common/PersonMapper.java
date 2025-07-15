package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.common.PersonSearchResultDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.mapper.simple.employee.EmployeeMapper;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionAdminMapper;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.application.mapper.wrappers.student.StudentMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {
        StudentMapperWrapper.class,
        TeacherMapper.class,
        EmployeeMapper.class,
        InstitutionAdminMapper.class
})
public interface PersonMapper {

    @SubclassMapping(source = Student.class, target = StudentResponseDto.class)
    @SubclassMapping(source = Teacher.class, target = TeacherResponseDto.class)
    @SubclassMapping(source = Employee.class, target = EmployeeResponseDto.class)
    @SubclassMapping(source = InstitutionAdmin.class, target = InstitutionAdminResponseDto.class)
    PersonResponseDto toResponseDto(Person person);

    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName())")
    @Mapping(target = "personType", expression = "java(person.getPersonType().getDisplayName())")
    PersonSearchResultDto toSearchResultDto(Person person);

    @Mapping(target = "fullName", source = "person", qualifiedByName = "personToFullName")
    PersonSummaryDto toSummaryDto(Person person);

    @Named("personToFullName")
    default String personToFullName(Person person) {
        if (person == null || person.getFirstName() == null || person.getLastName() == null) {
            return "";
        }
        return person.getFirstName() + " " + person.getLastName();
    }

    @ObjectFactory
    default PersonResponseDto resolvePersonSubtype(Person person) {
        Person real = (Person) org.hibernate.Hibernate.unproxy(person);

        if (real instanceof Student) return new StudentResponseDto();
        if (real instanceof Teacher) return new TeacherResponseDto();
        if (real instanceof Employee) return new EmployeeResponseDto();
        if (real instanceof InstitutionAdmin) return new InstitutionAdminResponseDto();

        throw new IllegalArgumentException("Tipo desconhecido de Person: " + real.getClass());
    }
}
