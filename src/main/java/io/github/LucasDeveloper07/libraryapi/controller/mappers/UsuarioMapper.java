package io.github.LucasDeveloper07.libraryapi.controller.mappers;

import io.github.LucasDeveloper07.libraryapi.controller.dto.UsuarioDTO;
import io.github.LucasDeveloper07.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
