package com.institution.management.academic_api.application.dto.library;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar um item do acervo.")
public record UpdateLibraryItemRequestDto(
        @Schema(description = "Novo título do item.")
        String title,

        @Schema(description = "Novo autor do item.")
        String author,

        @Schema(description = "Novo ISBN.")
        String isbn,

        @Schema(description = "Nova editora.")
        String publisher,

        @Schema(description = "Novo ano de publicação.")
        Integer publicationYear,

        @Schema(description = "Novo número total de cópias.")
        Integer totalCopies
) {}