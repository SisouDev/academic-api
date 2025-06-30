package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.student.UpdateStudentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.AddressMapper;
import com.institution.management.academic_api.application.mapper.wrappers.student.EnrollmentMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.common.Address;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import org.mapstruct.*;

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

    @Named("formatAddressToString")
    default String formatAddressToString(Address address) {
        if (address == null) return null;
        return String.format("%s, %s - %s, %s", address.getStreet(), address.getNumber(), address.getCity(), address.getState());
    }


}