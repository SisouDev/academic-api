package com.institution.management.academic_api.application.controller.tasks;

import com.institution.management.academic_api.application.dto.tasks.CreateTaskRequestDto;
import com.institution.management.academic_api.application.dto.tasks.TaskDetailsDto;
import com.institution.management.academic_api.application.dto.tasks.UpdateTaskRequestDto;
import com.institution.management.academic_api.domain.service.tasks.TaskService;
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
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints para gerenciamento de tarefas internas")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Cria uma nova tarefa para um departamento")
    public ResponseEntity<EntityModel<TaskDetailsDto>> create(@RequestBody CreateTaskRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorEmail = authentication.getName();
        TaskDetailsDto createdTask = taskService.create(request, creatorEmail);
        EntityModel<TaskDetailsDto> resource = EntityModel.of(createdTask,
                linkTo(methodOn(TaskController.class).findById(createdTask.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza uma tarefa existente")
    public ResponseEntity<EntityModel<TaskDetailsDto>> update(@PathVariable Long id, @RequestBody UpdateTaskRequestDto request) {
        TaskDetailsDto updatedTask = taskService.update(id, request);
        EntityModel<TaskDetailsDto> resource = EntityModel.of(updatedTask,
                linkTo(methodOn(TaskController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma tarefa")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de uma tarefa pelo ID")
    public ResponseEntity<EntityModel<TaskDetailsDto>> findById(@PathVariable Long id) {
        TaskDetailsDto task = taskService.findById(id);
        EntityModel<TaskDetailsDto> resource = EntityModel.of(task,
                linkTo(methodOn(TaskController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Lista todas as tarefas de um departamento específico")
    public ResponseEntity<CollectionModel<EntityModel<TaskDetailsDto>>> findByDepartment(@PathVariable Long departmentId) {
        List<TaskDetailsDto> tasks = taskService.findByDepartment(departmentId);

        List<EntityModel<TaskDetailsDto>> resources = tasks.stream()
                .map(task -> EntityModel.of(task,
                        linkTo(methodOn(TaskController.class).findById(task.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(TaskController.class).findByDepartment(departmentId)).withSelfRel()));
    }
}