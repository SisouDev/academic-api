package com.institution.management.academic_api.application.controller.financial;

import com.institution.management.academic_api.application.dto.financial.PayableSummaryDto;
import com.institution.management.academic_api.domain.service.financial.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
@Tag(name = "Finance Reports", description = "Endpoints para relatórios e visões financeiras unificadas")
@PreAuthorize("hasAnyRole('FINANCE_MANAGER', 'FINANCE_ASSISTANT', 'ADMIN')")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/payables")
    @Operation(summary = "Busca uma lista unificada de todas as contas a pagar pendentes")
    public ResponseEntity<CollectionModel<PayableSummaryDto>> getPendingPayables() {
        List<PayableSummaryDto> payables = financeService.getPendingPayables();

        CollectionModel<PayableSummaryDto> resource = CollectionModel.of(payables,
                linkTo(methodOn(FinanceController.class).getPendingPayables()).withSelfRel()
        );

        return ResponseEntity.ok(resource);
    }
}