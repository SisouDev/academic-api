package com.institution.management.academic_api.domain.service.library;

import com.institution.management.academic_api.application.dto.library.CreateLibraryItemRequestDto;
import com.institution.management.academic_api.application.dto.library.LibraryItemDetailsDto;
import com.institution.management.academic_api.application.dto.library.UpdateLibraryItemRequestDto;

import java.util.List;

public interface LibraryItemService {
    LibraryItemDetailsDto create(CreateLibraryItemRequestDto dto);

    LibraryItemDetailsDto update(Long id, UpdateLibraryItemRequestDto dto);

    void delete(Long id);

    LibraryItemDetailsDto findById(Long id);

    List<LibraryItemDetailsDto> findAll();
}
