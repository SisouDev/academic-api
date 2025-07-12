package com.institution.management.academic_api.application.mapper.simple.file;

import com.institution.management.academic_api.application.dto.file.FileUploadResponseDto;
import com.institution.management.academic_api.application.dto.file.FileUploadSummaryDto;
import com.institution.management.academic_api.domain.model.entities.file.FileUpload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {

    @Mapping(target = "fileDownloadUri", expression = "java(buildDownloadUri(fileUpload.getStoredFileName()))")
    FileUploadResponseDto toResponseDto(FileUpload fileUpload);

    @Mapping(target = "fileDownloadUri", expression = "java(buildDownloadUri(fileUpload.getStoredFileName()))")
    FileUploadSummaryDto toSummaryDto(FileUpload fileUpload);

    default String buildDownloadUri(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();
    }
}