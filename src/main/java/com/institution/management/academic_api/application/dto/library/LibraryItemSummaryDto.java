package com.institution.management.academic_api.application.dto.library;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de um item da biblioteca.")
public record LibraryItemSummaryDto(
        @Schema(description = "ID único do item.", example = "1")
        Long id,

        @Schema(description = "Título do item.", example = "O Senhor dos Anéis: A Sociedade do Anel")
        String title,

        @Schema(description = "Autor do item.", example = "J.R.R. Tolkien")
        String author,

        @Schema(description = "Tipo do item.", example = "Livro")
        String type,

        @Schema(description = "Cópias disponíveis para empréstimo.", example = "3")
        int availableCopies
) {}