package io.github.LucasDeveloper07.libraryapi.controller.mappers;

import io.github.LucasDeveloper07.libraryapi.controller.dto.ClientDTO;
import io.github.LucasDeveloper07.libraryapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);
}
