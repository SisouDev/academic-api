package com.institution.management.academic_api.application.controller.library;

import com.institution.management.academic_api.application.dto.library.CreateLoanRequestDto;
import com.institution.management.academic_api.application.dto.library.LoanDetailsDto;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.service.library.LoanService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "Library Loans", description = "Endpoints para gerenciamento de empréstimos de itens da biblioteca")
public class LoanController {

    private final LoanService loanService;
    private final PersonRepository personRepository;

    @PostMapping
    @Operation(summary = "Registra um novo empréstimo de um item")
    public ResponseEntity<EntityModel<LoanDetailsDto>> create(@RequestBody CreateLoanRequestDto request) {
        LoanDetailsDto createdLoan = loanService.create(request);
        EntityModel<LoanDetailsDto> resource = EntityModel.of(createdLoan,
                linkTo(methodOn(LoanController.class).findById(createdLoan.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Registra a devolução de um item emprestado")
    public ResponseEntity<EntityModel<LoanDetailsDto>> returnLoan(@PathVariable Long id) {
        LoanDetailsDto returnedLoan = loanService.returnLoan(id);
        EntityModel<LoanDetailsDto> resource = EntityModel.of(returnedLoan,
                linkTo(methodOn(LoanController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de um empréstimo pelo ID")
    public ResponseEntity<EntityModel<LoanDetailsDto>> findById(@PathVariable Long id) {
        LoanDetailsDto loan = loanService.findById(id);
        EntityModel<LoanDetailsDto> resource = EntityModel.of(loan,
                linkTo(methodOn(LoanController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-loans")
    @Operation(summary = "Lista todos os empréstimos do usuário logado")
    public ResponseEntity<CollectionModel<EntityModel<LoanDetailsDto>>> findMyLoans() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Person user = personRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        List<LoanDetailsDto> loans = loanService.findByBorrower(user.getId());
        List<EntityModel<LoanDetailsDto>> resources = loans.stream()
                .map(loan -> EntityModel.of(loan,
                        linkTo(methodOn(LoanController.class).findById(loan.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(LoanController.class).findMyLoans()).withSelfRel()));
    }
}