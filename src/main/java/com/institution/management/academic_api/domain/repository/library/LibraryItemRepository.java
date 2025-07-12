package com.institution.management.academic_api.domain.repository.library;

import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.enums.library.LibraryItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryItemRepository extends JpaRepository <LibraryItem, Long>{
    Optional<LibraryItem> findByIsbn(String isbn);

    List<LibraryItem> findByType(LibraryItemType type);

    List<LibraryItem> findByTitleContainingIgnoreCase(String title);
}
