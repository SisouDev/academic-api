package com.institution.management.academic_api.application.service.absence;

import com.institution.management.academic_api.application.dto.absence.AbsenceDetailsDto;
import com.institution.management.academic_api.application.dto.absence.CreateAbsenceRequestDto;
import com.institution.management.academic_api.application.dto.absence.ReviewAbsenceRequestDto;
import com.institution.management.academic_api.application.mapper.simple.absence.AbsenceMapper;
import com.institution.management.academic_api.application.notifiers.absence.AbsenceNotifier;
import com.institution.management.academic_api.application.service.common.FileStorageService;
import com.institution.management.academic_api.domain.factory.absence.AbsenceFactory;
import com.institution.management.academic_api.domain.factory.file.FileUploadFactory;
import com.institution.management.academic_api.domain.model.entities.absence.Absence;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.file.FileUpload;
import com.institution.management.academic_api.domain.model.enums.absence.AbsenceStatus;
import com.institution.management.academic_api.domain.model.enums.file.ReferenceType;
import com.institution.management.academic_api.domain.repository.absence.AbsenceRepository;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.file.FileUploadRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.absence.AbsenceService;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final PersonRepository personRepository;
    private final AbsenceFactory absenceFactory;
    private final AbsenceMapper absenceMapper;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final FileUploadFactory fileUploadFactory;
    private final FileUploadRepository fileUploadRepository;
    private final AbsenceNotifier absenceNotifier;

    @Override
    @Transactional
    @LogActivity("Registrou uma nova justificativa de ausência/atraso.")
    public AbsenceDetailsDto create(CreateAbsenceRequestDto dto) {
        Absence newAbsence = absenceFactory.create(dto);
        Absence savedAbsence = absenceRepository.save(newAbsence);
        absenceNotifier.notifyNewAbsenceCreated(savedAbsence);
        return absenceMapper.toDetailsDto(savedAbsence);
    }

    @Override
    @Transactional
    @LogActivity("Analisou uma justificativa de ausência.")
    public void review(Long absenceId, ReviewAbsenceRequestDto dto, String reviewerEmail) {
        Absence absenceToReview = findAbsenceByIdOrThrow(absenceId);
        Person reviewer = personRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Reviewer not found with email: " + reviewerEmail));
        absenceToReview.setStatus(AbsenceStatus.valueOf(dto.status()));
        absenceToReview.setReviewedBy(reviewer);
        absenceToReview.setReviewedAt(LocalDateTime.now());
        absenceNotifier.notifyRequesterOfReview(absenceToReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbsenceDetailsDto> findAll(AbsenceStatus status, Pageable pageable) {
        Page<Absence> page;
        if (status != null) {
            page = absenceRepository.findAllByStatus(status, pageable);
        } else {
            page = absenceRepository.findAll(pageable);
        }
        return page.map(absenceMapper::toDetailsDto);
    }


    @Override
    @Transactional
    @LogActivity("Adicionou um anexo a uma justificativa de ausência.")
    public String addAttachment(Long absenceId, MultipartFile file) {
        Absence absence = findAbsenceByIdOrThrow(absenceId);
        FileUpload fileUpload = fileUploadFactory.create(
                file,
                absenceId,
                ReferenceType.ABSENCE_JUSTIFICATION,
                "uploads/absences"
        );

        fileStorageService.store(file, fileUpload.getStoredFileName());
        fileUploadRepository.save(fileUpload);

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileUpload.getStoredFileName())
                .toUriString();

        absence.setAttachmentUrl(fileUrl);
        absenceNotifier.notifyReviewerOfNewAttachment(absence);
        return fileUrl;
    }

    @Override
    public AbsenceDetailsDto findById(Long id) {
        return absenceRepository.findById(id)
                .map(absenceMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found with id: " + id));
    }

    @Override
    public List<AbsenceDetailsDto> findByPerson(Long personId) {
        return absenceRepository.findByPersonId(personId)
                .stream()
                .map(absenceMapper::toDetailsDto)
                .toList();
    }

    private Absence findAbsenceByIdOrThrow(Long id) {
        return absenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found with id: " + id));
    }

}
