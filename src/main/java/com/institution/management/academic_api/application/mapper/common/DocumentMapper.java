package com.institution.management.academic_api.application.mapper.common;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import com.institution.management.academic_api.domain.model.shared.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    DocumentDto toDto(Document document);

    Document toEntity(DocumentDto documentDto);
}