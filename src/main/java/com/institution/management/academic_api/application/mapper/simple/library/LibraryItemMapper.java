package com.institution.management.academic_api.application.mapper.simple.library;

import com.institution.management.academic_api.application.dto.library.CreateLibraryItemRequestDto;
import com.institution.management.academic_api.application.dto.library.LibraryItemDetailsDto;
import com.institution.management.academic_api.application.dto.library.LibraryItemSummaryDto;
import com.institution.management.academic_api.application.dto.library.UpdateLibraryItemRequestDto;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LibraryItemMapper {

    @Mapping(target = "type", expression = "java(item.getType().getDisplayName())")
    LibraryItemSummaryDto toSummaryDto(LibraryItem item);

    @Mapping(target = "type", expression = "java(item.getType().getDisplayName())")
    LibraryItemDetailsDto toDetailsDto(LibraryItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalCopies", ignore = true)
    @Mapping(target = "availableCopies", ignore = true)
    LibraryItem toEntity(CreateLibraryItemRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableCopies", ignore = true)
    @Mapping(target = "type", ignore = true)
    void updateFromDto(UpdateLibraryItemRequestDto dto, @MappingTarget LibraryItem entity);
}