package com.institution.management.academic_api.application.notifiers.inventory;

import com.institution.management.academic_api.domain.model.entities.inventory.Material;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MaterialNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;


    public void notifyAdminOfNewMaterial(Material material) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);

        String message = String.format("Novo material cadastrado no catálogo: %s", material.getName());
        String link = "/admin/inventory/materials/" + material.getId();

        adminsToNotify.forEach(admin ->
                notificationService.createNotification(
                        admin,
                        message,
                        link,
                        NotificationType.GENERAL_INFO
                )
        );
    }
}