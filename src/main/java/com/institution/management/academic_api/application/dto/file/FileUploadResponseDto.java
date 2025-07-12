package com.institution.management.academic_api.application.dto.file;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resposta da API com os detalhes de um arquivo que foi enviado com sucesso.")
public record FileUploadResponseDto(
        @Schema(description = "ID único do registro do arquivo no banco de dados.", example = "1")
        Long id,

        @Schema(description = "URL completa para acessar ou baixar o arquivo.", example = "http://localhost:8080/uploads/arquivo_unico_123.pdf")
        String fileDownloadUri,

        @Schema(description = "Nome original do arquivo, como enviado pelo usuário.", example = "atestado_medico.pdf")
        String originalFileName,

        @Schema(description = "Tipo MIME do arquivo.", example = "application/pdf")
        String fileType,

        @Schema(description = "Tamanho do arquivo em bytes.", example = "1572864")
        Long fileSize,

        @Schema(description = "Data e hora do upload.")
        LocalDateTime createdAt
) {}