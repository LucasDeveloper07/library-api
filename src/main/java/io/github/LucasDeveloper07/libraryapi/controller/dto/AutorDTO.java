package io.github.LucasDeveloper07.libraryapi.controller.dto;

import io.github.LucasDeveloper07.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AutorDTO(
    @NotBlank(message = "Campo obrigatório!")
        String nome,

        @NotNull(message = "Campo obrigatório!")
        LocalDate dataNascimento,

        @NotBlank(message = "Campo obrigatório!")
        String nacionalidade) {

    public Autor mapearAutor() {
        Autor autor = new Autor();

        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);

        return autor;
    }
}
