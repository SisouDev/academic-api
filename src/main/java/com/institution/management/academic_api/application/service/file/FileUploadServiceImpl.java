package com.institution.management.academic_api.application.service.file;

import com.institution.management.academic_api.application.dto.file.FileUploadResponseDto;
import com.institution.management.academic_api.application.mapper.simple.file.FileUploadMapper;
import com.institution.management.academic_api.application.service.common.FileStorageService;
import com.institution.management.academic_api.domain.factory.file.FileUploadFactory;
import com.institution.management.academic_api.domain.model.entities.file.FileUpload;
import com.institution.management.academic_api.domain.model.enums.file.ReferenceType;
import com.institution.management.academic_api.domain.repository.file.FileUploadRepository;
import com.institution.management.academic_api.domain.service.file.FileUploadService;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileUploadRepository fileUploadRepository;
    private final FileUploadFactory fileUploadFactory;
    private final FileUploadMapper fileUploadMapper;
    private final FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    @Transactional
    @LogActivity("Fez upload de um novo arquivo.")
    public FileUploadResponseDto uploadFile(MultipartFile file, Long referenceId, ReferenceType referenceType) {
        FileUpload fileUpload = fileUploadFactory.create(file, referenceId, referenceType, uploadDir);
        fileStorageService.store(file, fileUpload.getStoredFileName());

        FileUpload savedFileUpload = fileUploadRepository.save(fileUpload);

        return fileUploadMapper.toResponseDto(savedFileUpload);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadResponseDto> findFilesByReference(Long referenceId, ReferenceType referenceType) {
        return fileUploadRepository.findByReferenceIdAndReferenceType(referenceId, referenceType)
                .stream()
                .map(fileUploadMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
