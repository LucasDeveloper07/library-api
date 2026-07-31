package io.github.LucasDeveloper07.libraryapi.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AutorResponseDTO(
        UUID id,
        String nome,
        LocalDate dataNascimento,
        String nacionalidade
) {
}
