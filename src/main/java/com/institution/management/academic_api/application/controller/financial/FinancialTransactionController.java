package com.institution.management.academic_api.application.controller.financial;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.dto.financial.FinancialTransactionDetailsDto;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.service.financial.FinancialTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/financial-transactions")
@RequiredArgsConstructor
@Tag(name = "Financial", description = "Endpoints para gerenciamento financeiro de alunos")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    @PostMapping("/transactions")
    @Operation(summary = "Registra uma nova transação financeira para um aluno")
    public ResponseEntity<EntityModel<FinancialTransactionDetailsDto>> create(@RequestBody CreateFinancialTransactionRequestDto request) {
        FinancialTransactionDetailsDto createdTransaction = transactionService.create(request);
        EntityModel<FinancialTransactionDetailsDto> resource = EntityModel.of(createdTransaction,
                linkTo(methodOn(FinancialTransactionController.class).findTransactionById(createdTransaction.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma transação financeira pelo ID")
    public ResponseEntity<EntityModel<FinancialTransactionDetailsDto>> findTransactionById(@PathVariable Long id) {
        FinancialTransactionDetailsDto transaction = transactionService.findById(id);
        EntityModel<FinancialTransactionDetailsDto> resource = EntityModel.of(transaction,
                linkTo(methodOn(FinancialTransactionController.class).findTransactionById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/students/{studentId}/statement")
    @Operation(summary = "Busca o extrato financeiro de um aluno")
    public ResponseEntity<CollectionModel<EntityModel<FinancialTransactionDetailsDto>>> getStudentStatement(@PathVariable Long studentId) {
        List<FinancialTransactionDetailsDto> transactions = transactionService.findTransactionsByPerson(studentId);
        List<EntityModel<FinancialTransactionDetailsDto>> resources = transactions.stream()
                .map(transaction -> EntityModel.of(transaction,
                        linkTo(methodOn(FinancialTransactionController.class).findTransactionById(transaction.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(FinancialTransactionController.class).getStudentStatement(studentId)).withSelfRel()));
    }

    @GetMapping("/students/{studentId}/balance")
    @Operation(summary = "Busca o saldo financeiro atual de um aluno")
    public ResponseEntity<EntityModel<BigDecimal>> getStudentBalance(@PathVariable Long studentId) {
        BigDecimal balance = transactionService.getPersonBalance(studentId);
        EntityModel<BigDecimal> resource = EntityModel.of(balance,
                linkTo(methodOn(FinancialTransactionController.class).getStudentBalance(studentId)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @Operation(summary = "Lista todas as transações financeiras com filtros")
    public ResponseEntity<PagedModel<EntityModel<FinancialTransactionDetailsDto>>> findAll(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            Pageable pageable,
            PagedResourcesAssembler<FinancialTransactionDetailsDto> assembler) {

        Page<FinancialTransactionDetailsDto> page = transactionService.findAll(type, status, pageable);
        return ResponseEntity.ok(assembler.toModel(page, transaction ->
                EntityModel.of(transaction,
                        linkTo(methodOn(FinancialTransactionController.class).findTransactionById(transaction.id())).withSelfRel())));
    }

    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @Operation(summary = "Marca uma multa (fine) como paga")
    public ResponseEntity<EntityModel<FinancialTransactionDetailsDto>> markAsPaid(@PathVariable Long id) {
        FinancialTransactionDetailsDto paidTransaction = transactionService.markAsPaid(id);
        EntityModel<FinancialTransactionDetailsDto> resource = EntityModel.of(paidTransaction,
                linkTo(methodOn(FinancialTransactionController.class).findTransactionById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }
}