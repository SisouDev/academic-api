package com.institution.management.academic_api.application.dto.file;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de um arquivo enviado.")
public record FileUploadSummaryDto(
        @Schema(description = "ID único do registro do arquivo.", example = "1")
        Long id,

        @Schema(description = "URL para download do arquivo.", example = "http://localhost:8080/uploads/arquivo_unico_123.pdf")
        String fileDownloadUri,

        @Schema(description = "Nome original do arquivo.", example = "atestado_medico.pdf")
        String originalFileName
) {}
