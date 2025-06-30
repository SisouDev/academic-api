package com.institution.management.academic_api.application.controller.employee;

import com.institution.management.academic_api.application.controller.institution.InstitutionController;
import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeSummaryDto;
import com.institution.management.academic_api.application.dto.employee.UpdateEmployeeRequestDto;
import com.institution.management.academic_api.domain.service.employee.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(@RequestBody @Valid CreateEmployeeRequestDto request) {
        EmployeeResponseDto createdEmployee = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PersonResponseDto>> findById(@PathVariable Long id) {
        PersonResponseDto employee = employeeService.findById(id);

        EntityModel<PersonResponseDto> model = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(InstitutionController.class).findById(employee.getInstitution().id())).withRel("institution")
        );

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateEmployeeRequestDto request) {
        EmployeeResponseDto updatedEmployee = employeeService.update(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<EmployeeSummaryDto>>> getAllEmployees(
            @RequestParam(required = false) Long institutionId,
            Pageable pageable,
            PagedResourcesAssembler<EmployeeSummaryDto> assembler
    ) {
        Page<EmployeeSummaryDto> employeesPage = employeeService.findPaginated(institutionId, pageable);

        PagedModel<EntityModel<EmployeeSummaryDto>> pagedModel = assembler.toModel(employeesPage,
                employee -> EntityModel.of(employee,
                        linkTo(methodOn(EmployeeController.class).findById(employee.id())).withSelfRel()
                )
        );

        return ResponseEntity.ok(pagedModel);
    }
}