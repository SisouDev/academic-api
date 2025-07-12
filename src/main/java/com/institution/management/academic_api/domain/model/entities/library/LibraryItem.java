package com.institution.management.academic_api.domain.model.entities.library;

import com.institution.management.academic_api.domain.model.enums.library.LibraryItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "library_items")
@Getter
@Setter
public class LibraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    private String isbn;

    private String publisher;

    private int publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LibraryItemType type;

    @Column(nullable = false)
    private int totalCopies;

    @Column(nullable = false)
    private int availableCopies;
}