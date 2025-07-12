package com.institution.management.academic_api.domain.factory.file;

import com.institution.management.academic_api.domain.model.entities.file.FileUpload;
import com.institution.management.academic_api.domain.model.enums.file.ReferenceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileUploadFactory {

    public FileUpload create(MultipartFile file, Long referenceId, ReferenceType referenceType, String storagePath) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Invalid or unnamed file.");
        }

        FileUpload fileUpload = new FileUpload();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String storedFileName = UUID.randomUUID().toString() + "." + fileExtension;

        fileUpload.setOriginalFileName(originalFilename);
        fileUpload.setStoredFileName(storedFileName);
        fileUpload.setFileType(file.getContentType());
        fileUpload.setFileSize(file.getSize());
        fileUpload.setStoragePath(storagePath);

        fileUpload.setReferenceId(referenceId);
        fileUpload.setReferenceType(referenceType);

        fileUpload.setCreatedAt(LocalDateTime.now());

        return fileUpload;
    }
}