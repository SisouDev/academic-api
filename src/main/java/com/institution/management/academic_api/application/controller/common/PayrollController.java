package com.institution.management.academic_api.application.controller.common;

import com.institution.management.academic_api.application.dto.common.PayrollRecordDetailsDto;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import com.institution.management.academic_api.domain.service.common.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll", description = "Endpoints para gerenciamento da Folha de Pagamento")
@PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
public class PayrollController {

    private final PayrollService payrollService;
    private final PagedResourcesAssembler<PayrollRecordDetailsDto> assembler;

    @GetMapping
    @Operation(summary = "Lista todos os registros da folha de pagamento com filtros")
    public ResponseEntity<PagedModel<EntityModel<PayrollRecordDetailsDto>>> findAll(
            @RequestParam(required = false) PayrollStatus status, Pageable pageable) {
        Page<PayrollRecordDetailsDto> page = payrollService.findAll(status, pageable);
        return ResponseEntity.ok(assembler.toModel(page,
                record -> EntityModel.of(record, linkTo(methodOn(PayrollController.class).findById(record.id())).withSelfRel())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um registro da folha de pagamento pelo ID")
    public ResponseEntity<EntityModel<PayrollRecordDetailsDto>> findById(@PathVariable Long id) {
        PayrollRecordDetailsDto record = payrollService.findById(id);
        EntityModel<PayrollRecordDetailsDto> resource = EntityModel.of(record,
                linkTo(methodOn(PayrollController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "Marca um registro da folha de pagamento como PAGO")
    public ResponseEntity<Void> markAsPaid(@PathVariable Long id) {
        payrollService.markAsPaid(id);
        return ResponseEntity.noContent().build();
    }
}