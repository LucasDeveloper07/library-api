package io.github.LucasDeveloper07.libraryapi.controller.mappers;

import io.github.LucasDeveloper07.libraryapi.controller.dto.AutorDTO;
import io.github.LucasDeveloper07.libraryapi.controller.dto.AutorResponseDTO;
import io.github.LucasDeveloper07.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO dto);

    AutorResponseDTO toDTO(Autor autor);
}
