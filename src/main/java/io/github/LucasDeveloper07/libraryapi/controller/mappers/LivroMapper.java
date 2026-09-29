package io.github.LucasDeveloper07.libraryapi.controller.mappers;

import io.github.LucasDeveloper07.libraryapi.controller.dto.LivroDTO;
import io.github.LucasDeveloper07.libraryapi.controller.dto.LivroResponseDTO;
import io.github.LucasDeveloper07.libraryapi.model.Livro;
import io.github.LucasDeveloper07.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(LivroDTO dto);

    public abstract LivroResponseDTO toDTO(Livro livro);
}
