package com.institution.management.academic_api.application.mapper.user;

import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.dto.user.UpdateUserStatusRequestDto;
import com.institution.management.academic_api.application.dto.user.UserResponseDto;
import com.institution.management.academic_api.application.dto.user.UserSummaryDto;
import com.institution.management.academic_api.application.mapper.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.common.RoleMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {
        PersonMapper.class,
        RoleMapper.class
})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toResponseDto(User user);

    @Mapping(target = "fullName", source = "person", qualifiedByName = "personToFullName")
    UserSummaryDto toSummaryDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(CreateUserRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateFromDto(UpdateUserStatusRequestDto dto, @MappingTarget User entity);


    @Named("personToFullName")
    default String personToFullName(Person person) {
        if (person == null) {
            return null;
        }
        return person.getFirstName() + " " + person.getLastName();
    }
}