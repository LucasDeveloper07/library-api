package io.github.LucasDeveloper07.libraryapi.controller;

import io.github.LucasDeveloper07.libraryapi.controller.dto.UsuarioDTO;
import io.github.LucasDeveloper07.libraryapi.controller.mappers.UsuarioMapper;
import io.github.LucasDeveloper07.libraryapi.model.Usuario;
import io.github.LucasDeveloper07.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class UsuarioController implements GenericController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = mapper.toEntity(dto);
        service.salvar(usuario);

        URI location = generatedHeaderLocation(usuario.getId());

        return ResponseEntity.created(location).build();
    }
}
