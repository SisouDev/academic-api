package com.institution.management.academic_api.application.controller.dashboard;

import com.institution.management.academic_api.application.dto.dashboard.admin.GlobalStatsDto;
import com.institution.management.academic_api.application.dto.dashboard.director.DirectorDashboardDto;
import com.institution.management.academic_api.application.dto.dashboard.employee.*;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/hr-analyst")
    @PreAuthorize("hasRole('HR_ANALYST')")
    @Operation(summary = "Busca os dados do dashboard específico para o Analista de RH")
    public ResponseEntity<EntityModel<HrAnalystDashboardDto>> getHrAnalystDashboard(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        HrAnalystDashboardDto dashboardData = dashboardService.getHrAnalystDashboard(authenticatedUser);

        EntityModel<HrAnalystDashboardDto> resource = EntityModel.of(dashboardData,
                linkTo(methodOn(DashboardController.class).getHrAnalystDashboard(authentication)).withSelfRel()
        );

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/technician")
    @PreAuthorize("hasRole('TECHNICIAN')")
    @Operation(summary = "Busca os dados do dashboard específico para o Técnico de TI")
    public ResponseEntity<EntityModel<TechnicianDashboardDto>> getTechnicianDashboard(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        TechnicianDashboardDto dashboardData = dashboardService.getTechnicianDashboard(authenticatedUser);

        EntityModel<TechnicianDashboardDto> resource = EntityModel.of(dashboardData,
                linkTo(methodOn(DashboardController.class).getTechnicianDashboard(authentication)).withSelfRel()
        );

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/global-stats")
    @Operation(summary = "Busca estatísticas globais da instituição")
    public ResponseEntity<EntityModel<GlobalStatsDto>> getGlobalStats() {
        GlobalStatsDto stats = dashboardService.getGlobalStats();

        EntityModel<GlobalStatsDto> resource = EntityModel.of(stats,
                linkTo(methodOn(DashboardController.class).getGlobalStats()).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/librarian")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Busca os dados do dashboard específico para o Bibliotecário")
    public ResponseEntity<EntityModel<LibrarianDashboardDto>> getLibrarianDashboard(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        LibrarianDashboardDto dashboardData = dashboardService.getLibrarianDashboard(authenticatedUser);

        EntityModel<LibrarianDashboardDto> resource = EntityModel.of(dashboardData,
                linkTo(methodOn(DashboardController.class).getLibrarianDashboard(authentication)).withSelfRel()
        );
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/finance")
    @PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER')")
    @Operation(summary = "Busca os dados do dashboard específico para o setor Financeiro")
    public ResponseEntity<EntityModel<FinanceDashboardDto>> getFinanceDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        FinanceDashboardDto dashboardData = dashboardService.getFinanceDashboard(user);
        EntityModel<FinanceDashboardDto> resource = EntityModel.of(dashboardData,
                linkTo(methodOn(DashboardController.class).getFinanceDashboard(authentication)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/secretary")
    @PreAuthorize("hasRole('SECRETARY')")
    @Operation(summary = "Busca os dados do dashboard específico para Secretário(a)")
    public ResponseEntity<EntityModel<SecretaryDashboardDto>> getSecretaryDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        SecretaryDashboardDto dashboardData = dashboardService.getSecretaryDashboard(user);
        EntityModel<SecretaryDashboardDto> resource = EntityModel.of(dashboardData,
                linkTo(methodOn(DashboardController.class).getSecretaryDashboard(authentication)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/director")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Busca os dados do dashboard estratégico para o Diretor")
    public ResponseEntity<EntityModel<DirectorDashboardDto>> getDirectorDashboard(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        DirectorDashboardDto dashboardData = dashboardService.getDirectorDashboard(authenticatedUser);

        EntityModel<DirectorDashboardDto> resource = EntityModel.of(dashboardData,
                linkTo(methodOn(DashboardController.class).getDirectorDashboard(authentication)).withSelfRel()
        );

        return ResponseEntity.ok(resource);
    }
}