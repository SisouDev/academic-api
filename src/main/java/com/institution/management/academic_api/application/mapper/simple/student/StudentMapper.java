package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.student.UpdateStudentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.AddressMapper;
import com.institution.management.academic_api.application.mapper.wrappers.student.EnrollmentMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, EnrollmentMapperWrapper.class})
public interface StudentMapper {

    StudentResponseDto toResponseDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "document", source = "document")
    @Mapping(target = "address.street", source = "address.street")
    @Mapping(target = "address.number", source = "address.number")
    @Mapping(target = "address.complement", source = "address.complement")
    @Mapping(target = "address.district", source = "address.district")
    @Mapping(target = "address.city", source = "address.city")
    @Mapping(target = "address.state", source = "address.state")
    @Mapping(target = "address.zipCode", source = "address.zipCode")
    @Mapping(target = "phone", ignore = true)
    Student toEntity(CreateStudentRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "phone", ignore = true)
    void updateFromDto(UpdateStudentRequestDto dto, @MappingTarget Student entity);
}