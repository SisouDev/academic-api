package com.institution.management.academic_api.application.dto.library;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação detalhada de um item da biblioteca.")
public record LibraryItemDetailsDto(
        @Schema(description = "ID único do item.", example = "1")
        Long id,

        @Schema(description = "Título do item.")
        String title,

        @Schema(description = "Autor do item.")
        String author,

        @Schema(description = "ISBN.")
        String isbn,

        @Schema(description = "Editora.")
        String publisher,

        @Schema(description = "Ano de publicação.")
        int publicationYear,

        @Schema(description = "Tipo do item.")
        String type,

        @Schema(description = "Total de cópias.")
        int totalCopies,

        @Schema(description = "Cópias disponíveis.")
        int availableCopies
) {}