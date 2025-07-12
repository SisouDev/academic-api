package com.institution.management.academic_api.domain.factory.library;

import com.institution.management.academic_api.application.dto.library.CreateLibraryItemRequestDto;
import com.institution.management.academic_api.application.mapper.simple.library.LibraryItemMapper;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryItemFactory {

    private final LibraryItemMapper libraryItemMapper;

    public LibraryItem create(CreateLibraryItemRequestDto dto) {
        LibraryItem item = libraryItemMapper.toEntity(dto);

        item.setTotalCopies(dto.totalCopies());
        item.setAvailableCopies(dto.totalCopies());

        return item;
    }
}