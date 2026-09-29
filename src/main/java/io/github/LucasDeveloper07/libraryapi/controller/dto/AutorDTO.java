package io.github.LucasDeveloper07.libraryapi.controller.dto;

import io.github.LucasDeveloper07.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AutorDTO(
        @NotBlank(message = "Campo obrigatório!")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho padrão.")
        String nome,

        @NotNull(message = "Campo obrigatório!")
        @Past(message = "Não pode ser uma data futura.")
        LocalDate dataNascimento,

        @NotBlank(message = "Campo obrigatório!")
        @Size(min = 2, max = 50, message = "Campo fora do tamanho padrão.")
        String nacionalidade) {
}
