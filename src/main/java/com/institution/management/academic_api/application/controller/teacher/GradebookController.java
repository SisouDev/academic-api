package com.institution.management.academic_api.application.controller.teacher;

import com.institution.management.academic_api.application.dto.teacher.GradebookDto;
import com.institution.management.academic_api.application.service.teacher.GradebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/gradebooks")
@RequiredArgsConstructor
public class GradebookController {

    private final GradebookService gradebookService;

    @GetMapping
    public ResponseEntity<EntityModel<GradebookDto>> getGradebook(@RequestParam Long courseSectionId) {
        GradebookDto gradebook = gradebookService.getGradebookForSection(courseSectionId);

        EntityModel<GradebookDto> model = EntityModel.of(gradebook,
                linkTo(methodOn(GradebookController.class).getGradebook(courseSectionId)).withSelfRel()
        );

        return ResponseEntity.ok(model);
    }
}