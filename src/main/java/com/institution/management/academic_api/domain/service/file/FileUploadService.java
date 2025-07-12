package com.institution.management.academic_api.domain.service.file;

import com.institution.management.academic_api.application.dto.file.FileUploadResponseDto;
import com.institution.management.academic_api.domain.model.enums.file.ReferenceType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    FileUploadResponseDto uploadFile(MultipartFile file, Long referenceId, ReferenceType referenceType);

    List<FileUploadResponseDto> findFilesByReference(Long referenceId, ReferenceType referenceType);

}
