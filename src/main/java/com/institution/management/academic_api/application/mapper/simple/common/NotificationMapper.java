package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.NotificationDto;
import com.institution.management.academic_api.domain.model.entities.common.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "isRead", expression = "java(notification.getStatus() == com.institution.management.academic_api.domain.model.enums.common.NotificationStatus.READ)")
    @Mapping(target = "type", source = "type.displayName")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "formatDateTime")
    NotificationDto toDto(Notification notification);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}