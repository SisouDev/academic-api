package com.institution.management.academic_api.application.dto.library;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar um novo item no acervo da biblioteca.")
public record CreateLibraryItemRequestDto(
        @Schema(description = "Título do item.", requiredMode = Schema.RequiredMode.REQUIRED, example = "O Senhor dos Anéis: A Sociedade do Anel")
        String title,

        @Schema(description = "Autor do item.", example = "J.R.R. Tolkien")
        String author,

        @Schema(description = "ISBN do livro.", example = "978-0-618-05326-7")
        String isbn,

        @Schema(description = "Editora.", example = "HarperCollins")
        String publisher,

        @Schema(description = "Ano de publicação.", example = "1954")
        int publicationYear,

        @Schema(description = "Tipo do item.", requiredMode = Schema.RequiredMode.REQUIRED, example = "BOOK")
        String type,

        @Schema(description = "Número total de cópias adquiridas.", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
        int totalCopies
) {}