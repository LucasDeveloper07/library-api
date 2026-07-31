package io.github.LucasDeveloper07.libraryapi.controller;

import io.github.LucasDeveloper07.libraryapi.controller.dto.AutorDTO;
import io.github.LucasDeveloper07.libraryapi.controller.dto.AutorResponseDTO;
import io.github.LucasDeveloper07.libraryapi.controller.dto.ErroResposta;
import io.github.LucasDeveloper07.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.LucasDeveloper07.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.LucasDeveloper07.libraryapi.model.Autor;
import io.github.LucasDeveloper07.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService service;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AutorDTO autor) {
        try {
            Autor autorEntity = autor.mapearAutor();
            service.salvar(autorEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntity.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            var erroDto = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDto.status()).body(erroDto);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorResponseDTO> obterDetalhes(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);

        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isPresent()) {
            Autor autor = autorOptional.get();

            AutorResponseDTO autorResponse = new AutorResponseDTO(autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade());

            return ResponseEntity.ok(autorResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarAutor(@PathVariable("id") String id) {
        try {
            var idAutor = UUID.fromString(id);

            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            service.deletar(autorOptional.get());

            return ResponseEntity.noContent().build();
        } catch (OperacaoNaoPermitidaException e) {
            var erroDeletar = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDeletar.status()).body(erroDeletar);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> pesquisaResultado = service.pesquisa(nome, nacionalidade);

        List<AutorResponseDTO> listDto = pesquisaResultado
                .stream()
                .map(autor -> new AutorResponseDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO autorDto) {

        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Autor autor = autorOptional.get();

            autor.setNome(autorDto.nome());
            autor.setDataNascimento(autorDto.dataNascimento());
            autor.setNacionalidade(autorDto.nacionalidade());

            service.atualizar(autor);

            return ResponseEntity.noContent().build();
        } catch (RegistroDuplicadoException e) {
            var erroDto = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDto.status()).body(erroDto);
        }
    }
}
