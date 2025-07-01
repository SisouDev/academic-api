package com.institution.management.academic_api.application.controller.common;

import com.institution.management.academic_api.application.controller.employee.EmployeeController;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.service.common.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;


    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "PEOPLE") String scope,
            Pageable pageable,
            PagedResourcesAssembler<PersonSummaryDto> assembler) {

        if (scope.equalsIgnoreCase("PEOPLE")) {
            Page<PersonSummaryDto> peoplePage = searchService.searchPeople(query, pageable);

            PagedModel<EntityModel<PersonSummaryDto>> pagedModel = assembler.toModel(peoplePage,
                    person -> EntityModel.of(person,
                            linkTo(methodOn(EmployeeController.class).findById(person.id())).withSelfRel()
                    )
            );
            return ResponseEntity.ok(pagedModel);
        }
        return ResponseEntity.badRequest().body("Escopo de busca inválido.");
    }
}