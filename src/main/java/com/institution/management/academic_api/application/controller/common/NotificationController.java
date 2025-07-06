package com.institution.management.academic_api.application.controller.common;

import com.institution.management.academic_api.application.dto.common.NotificationDto;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<CollectionModel<EntityModel<NotificationDto>>> getMyNotifications() {
        List<NotificationDto> notifications = notificationService.findNotificationsForCurrentUser();
        List<EntityModel<NotificationDto>> notificationModels = notifications.stream()
                .map(notification -> EntityModel.of(notification,
                        linkTo(methodOn(NotificationController.class).markAsRead(notification.id())).withRel("markAsRead")))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(NotificationController.class).getMyNotifications()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(notificationModels, selfLink));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}