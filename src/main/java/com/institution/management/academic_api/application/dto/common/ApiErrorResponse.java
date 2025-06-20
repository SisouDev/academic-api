package com.institution.management.academic_api.application.dto.common;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Estrutura padrão para respostas de erro da API")
public record ApiErrorResponse(

        @Schema(description = "O código de status HTTP.", example = "404")
        int status,

        @Schema(description = "A frase padrão para o status HTTP.", example = "Not Found")
        String error,

        @Schema(description = "Uma mensagem detalhada e amigável sobre o erro.", example = "Usuário com ID 123 não encontrado.")
        String message,

        @Schema(description = "O endpoint da API onde o erro ocorreu.", example = "/api/v1/users/123")
        String path,

        @Schema(description = "O momento exato em que o erro ocorreu.")
        LocalDateTime timestamp
) {
}