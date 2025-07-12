package com.institution.management.academic_api.domain.repository.file;

import com.institution.management.academic_api.domain.model.entities.file.FileUpload;
import com.institution.management.academic_api.domain.model.enums.file.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    List<FileUpload> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
}
