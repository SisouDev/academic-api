package com.institution.management.academic_api.application.controller.dashboard;

import com.institution.management.academic_api.application.dto.dashboard.admin.GlobalStatsDto;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para visualização de dados e estatísticas")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/data")
    @Operation(summary = "Busca os dados do dashboard de acordo com o papel do usuário logado")
    public ResponseEntity<?> getDashboardData(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();

        Object dashboardData = dashboardService.getDashboardDataForUser(authenticatedUser);

        return ResponseEntity.ok(dashboardData);
    }


    @GetMapping("/global-stats")
    @Operation(summary = "Busca estatísticas globais da instituição")
    public ResponseEntity<EntityModel<GlobalStatsDto>> getGlobalStats() {
        GlobalStatsDto stats = dashboardService.getGlobalStats();

        EntityModel<GlobalStatsDto> resource = EntityModel.of(stats,
                linkTo(methodOn(DashboardController.class).getGlobalStats()).withSelfRel());

        return ResponseEntity.ok(resource);
    }
}