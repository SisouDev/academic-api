package com.institution.management.academic_api.application.controller.common;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.domain.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
@Tag(name = "People", description = "Endpoints para obter informações gerais sobre pessoas")
public class PersonController {

    private final UserService userService;

    @GetMapping("/selectable-participants")
    @Operation(summary = "Busca uma lista de todas as pessoas que podem ser convidadas para uma reunião (não inclui alunos)")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PersonSummaryDto>> getSelectableParticipants() {
        List<PersonSummaryDto> participants = userService.findSelectableParticipants();
        return ResponseEntity.ok(participants);
    }
}