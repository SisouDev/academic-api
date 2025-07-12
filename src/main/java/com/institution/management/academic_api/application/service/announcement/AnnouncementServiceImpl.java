package com.institution.management.academic_api.application.service.announcement;

import com.institution.management.academic_api.application.dto.announcement.AnnouncementDetailsDto;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;
import com.institution.management.academic_api.application.dto.announcement.CreateAnnouncementRequestDto;
import com.institution.management.academic_api.application.dto.announcement.UpdateAnnouncementRequestDto;
import com.institution.management.academic_api.application.mapper.simple.announcement.AnnouncementMapper;
import com.institution.management.academic_api.application.notifiers.announcement.AnnouncementNotifier;
import com.institution.management.academic_api.domain.factory.announcement.AnnouncementFactory;
import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.repository.announcement.AnnouncementRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.service.announcement.AnnouncementService;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.employee.EmployeeNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final EmployeeRepository employeeRepository;
    private final AnnouncementFactory announcementFactory;
    private final AnnouncementMapper announcementMapper;
    private final NotificationService notificationService;
    private final AnnouncementNotifier announcementNotifier;

    @Override
    @Transactional
    @LogActivity("Publicou um novo aviso.")
    public AnnouncementDetailsDto create(CreateAnnouncementRequestDto dto, String creatorEmail) {
        Employee creator = findEmployeeByEmailOrThrow(creatorEmail);
        Announcement newAnnouncement = announcementFactory.create(dto, creator);
        Announcement savedAnnouncement = announcementRepository.save(newAnnouncement);
        announcementNotifier.notifyNewAnnouncement(savedAnnouncement);
        return announcementMapper.toDetailsDto(savedAnnouncement);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou o aviso.")
    public AnnouncementDetailsDto update(Long id, UpdateAnnouncementRequestDto dto) {
        Announcement existingAnnouncement = findAnnouncementByIdOrThrow(id);
        announcementMapper.updateFromDto(dto, existingAnnouncement);
        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);
        announcementNotifier.notifyNewAnnouncement(updatedAnnouncement);
        return announcementMapper.toDetailsDto(updatedAnnouncement);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um aviso.")
    public void delete(Long id) {
        Announcement existingAnnouncement = findAnnouncementByIdOrThrow(id);
        announcementNotifier.notifyNewAnnouncement(existingAnnouncement);
        announcementRepository.delete(existingAnnouncement);
    }

    @Override
    public AnnouncementDetailsDto findById(Long id) {
        Announcement announcement = findAnnouncementByIdOrThrow(id);
        return announcementMapper.toDetailsDto(announcement);
    }

    @Override
    public List<AnnouncementSummaryDto> findVisibleForCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentUser = employeeRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found."));

        Long departmentId = (currentUser.getDepartment() != null) ? currentUser.getDepartment().getId() : null;

        return announcementRepository.findVisibleAnnouncements(departmentId).stream()
                .map(announcementMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    private Employee findEmployeeByEmailOrThrow(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with email: " + email));
    }

    private Announcement findAnnouncementByIdOrThrow(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Announcement not found with id: " + id));
    }

}
