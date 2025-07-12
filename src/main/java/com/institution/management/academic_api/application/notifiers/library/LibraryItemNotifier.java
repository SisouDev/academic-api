package com.institution.management.academic_api.application.notifiers.library;

import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
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
public class LibraryItemNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyStaffOfNewItem(LibraryItem newItem) {
        List<User> staffToNotify = userRepository.findByRoles_NameIn(
                List.of(RoleName.ROLE_LIBRARIAN, RoleName.ROLE_ADMIN)
        );

        String message = String.format(
                "Novo item adicionado ao acervo: %s (%s)",
                newItem.getTitle(),
                newItem.getType().getDisplayName()
        );
        String link = "/admin/library/items/" + newItem.getId();

        staffToNotify.forEach(staff ->
                notificationService.createNotification(
                        staff,
                        message,
                        link,
                        NotificationType.GENERAL_INFO
                )
        );
    }
}