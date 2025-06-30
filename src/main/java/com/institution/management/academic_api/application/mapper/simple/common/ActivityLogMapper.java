package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.ActivityLogDto;
import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {

    @Mapping(target = "userName", source = "user.person.firstName")
    @Mapping(target = "userEmail", source = "user.login")
    @Mapping(target = "timestamp", expression = "java(log.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern(\"dd/MM/yyyy HH:mm\")))")
    ActivityLogDto toDto(ActivityLog log);
}