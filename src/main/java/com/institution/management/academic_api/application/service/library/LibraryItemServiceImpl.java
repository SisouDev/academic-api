package com.institution.management.academic_api.application.service.library;

import com.institution.management.academic_api.application.dto.library.CreateLibraryItemRequestDto;
import com.institution.management.academic_api.application.dto.library.LibraryItemDetailsDto;
import com.institution.management.academic_api.application.dto.library.UpdateLibraryItemRequestDto;
import com.institution.management.academic_api.application.mapper.simple.library.LibraryItemMapper;
import com.institution.management.academic_api.application.notifiers.library.LibraryItemNotifier;
import com.institution.management.academic_api.domain.factory.library.LibraryItemFactory;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.repository.library.LibraryItemRepository;
import com.institution.management.academic_api.domain.service.library.LibraryItemService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryItemServiceImpl implements LibraryItemService {

    private final LibraryItemRepository libraryItemRepository;
    private final LibraryItemFactory libraryItemFactory;
    private final LibraryItemMapper libraryItemMapper;
    private final LibraryItemNotifier libraryItemNotifier;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo item na biblioteca.")
    public LibraryItemDetailsDto create(CreateLibraryItemRequestDto dto) {
        if (dto.isbn() != null && !dto.isbn().isBlank()) {
            libraryItemRepository.findByIsbn(dto.isbn()).ifPresent(item -> {
                throw new EntityExistsException("An item with the ISBN already exists: " + dto.isbn());
            });
        }

        LibraryItem newItem = libraryItemFactory.create(dto);
        LibraryItem savedItem = libraryItemRepository.save(newItem);

        libraryItemNotifier.notifyStaffOfNewItem(savedItem);

        return libraryItemMapper.toDetailsDto(savedItem);
    }


    @Override
    @Transactional
    @LogActivity("Atualizou um item da biblioteca.")
    public LibraryItemDetailsDto update(Long id, UpdateLibraryItemRequestDto dto) {
        LibraryItem item = libraryItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));

        libraryItemMapper.updateFromDto(dto, item);
        return libraryItemMapper.toDetailsDto(item);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um item da biblioteca.")
    public void delete(Long id) {
        LibraryItem item = libraryItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));

        if (item.getAvailableCopies() < item.getTotalCopies()) {
            throw new IllegalStateException("It is not possible to delete the item as there are copies on loan.");
        }
        libraryItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LibraryItemDetailsDto findById(Long id) {
        return libraryItemRepository.findById(id)
                .map(libraryItemMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibraryItemDetailsDto> findAll() {
        return libraryItemRepository.findAll().stream()
                .map(libraryItemMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}
