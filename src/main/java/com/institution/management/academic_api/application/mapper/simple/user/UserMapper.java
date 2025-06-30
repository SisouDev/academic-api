package com.institution.management.academic_api.application.mapper.simple.user;

import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.dto.user.UpdateUserStatusRequestDto;
import com.institution.management.academic_api.application.dto.user.UserResponseDto;
import com.institution.management.academic_api.application.dto.user.UserSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.common.RoleMapper;
import com.institution.management.academic_api.application.mapper.wrappers.common.PersonMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        PersonMapperWrapper.class,
        RoleMapper.class
})
public interface UserMapper {

    @Mapping(target = "person", source = "person")
    @Mapping(target = "roles", source = "roles")
    UserResponseDto toResponseDto(User user);

    @Mapping(target = "fullName", source = "person", qualifiedByName = "personToFullName")
    UserSummaryDto toSummaryDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(CreateUserRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateUserFromDto(UpdateUserStatusRequestDto dto, @MappingTarget User user);

    @Named("rolesToRoleNames")
    default Set<String> rolesToRoleNames(Set<Role> roles) {
        if (roles == null) return Collections.emptySet();
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

}