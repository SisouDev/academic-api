package com.institution.management.academic_api.application.dto.common;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa um endereço.")
public record AddressDto(
        @Schema(description = "Nome da rua.", example = "Rua das Flores")
        String street,

        @Schema(description = "Número do imóvel.", example = "123")
        String number,

        @Schema(description = "Complemento do endereço.", example = "Apto 45")
        String complement,

        @Schema(description = "Bairro.", example = "Centro")
        String district,

        @Schema(description = "Cidade.", example = "São Paulo")
        String city,

        @Schema(description = "Estado.", example = "SP")
        String state,

        @Schema(description = "CEP.", example = "01000-000")
        String zipCode
) {}