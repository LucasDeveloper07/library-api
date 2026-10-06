package io.github.LucasDeveloper07.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Client")
public record ClientDTO(

        @NotBlank(message = "campo obrigatório")
        String clientId,

        @NotBlank(message = "campo obrigatório")
        String clientSecret,

        @NotBlank(message = "campo obrigatório")
        String redirectURI,

        @NotBlank(message = "campo obrigatório")
        String scope
) {
}
