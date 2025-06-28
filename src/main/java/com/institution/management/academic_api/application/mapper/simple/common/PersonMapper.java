package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
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
import org.hibernate.Hibernate;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {
        StudentMapperWrapper.class,
        TeacherMapper.class,
        EmployeeMapper.class,
        InstitutionAdminMapper.class
})
public interface PersonMapper {

    @Mapping(target = "profilePictureUrl", source = "profilePictureUrl")
    @SubclassMapping(source = Student.class, target = StudentResponseDto.class)
    @SubclassMapping(source = Teacher.class, target = TeacherResponseDto.class)
    @SubclassMapping(source = Employee.class, target = EmployeeResponseDto.class)
    @SubclassMapping(source = InstitutionAdmin.class, target = InstitutionAdminResponseDto.class)
    PersonResponseDto toResponseDto(Person person);

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
    default PersonResponseDto resolvePersonSubclass(Person person) {
        final Object realObject = Hibernate.unproxy(person);

        return switch (realObject) {
            case Student student -> new StudentResponseDto();
            case Teacher teacher -> new TeacherResponseDto();
            case Employee employee -> new EmployeeResponseDto();
            case InstitutionAdmin institutionAdmin -> new InstitutionAdminResponseDto();
            default -> throw new IllegalArgumentException("Unknown person subtype: " + realObject.getClass());
        };
    }
}