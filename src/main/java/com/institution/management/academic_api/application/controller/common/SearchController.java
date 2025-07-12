package com.institution.management.academic_api.application.controller.common;

import com.institution.management.academic_api.application.dto.common.SearchResponseDto;
import com.institution.management.academic_api.application.service.common.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponseDto> search(@RequestParam String query) {
        return ResponseEntity.ok(searchService.search(query));
    }
}