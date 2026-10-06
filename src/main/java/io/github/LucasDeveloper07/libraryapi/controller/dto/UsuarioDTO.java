package io.github.LucasDeveloper07.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "Usuário")
public record UsuarioDTO(
        @NotBlank(message = "campo obrigatório")
        String login,

        @NotBlank(message = "campo obrigatório")
        String senha,

        @NotBlank(message = "campo obrigatório")
        @Email(message = "inválido")
        String email,

        List<String> roles
) {
}
