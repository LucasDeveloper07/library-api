package io.github.LucasDeveloper07.libraryapi.controller;

import io.github.LucasDeveloper07.libraryapi.controller.dto.LivroDTO;
import io.github.LucasDeveloper07.libraryapi.controller.dto.LivroResponseDTO;
import io.github.LucasDeveloper07.libraryapi.controller.mappers.LivroMapper;
import io.github.LucasDeveloper07.libraryapi.model.GeneroLivro;
import io.github.LucasDeveloper07.libraryapi.model.Livro;
import io.github.LucasDeveloper07.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Void> save(@RequestBody @Valid LivroDTO dto) {
        Livro livroEntity = mapper.toEntity(dto);
        service.salvar(livroEntity);

        URI location = generatedHeaderLocation(livroEntity.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<LivroResponseDTO> obterDetalhes(@PathVariable("id") String id) {
        var idLivro = UUID.fromString(id);

        return service
                .obterPorId(idLivro)
                .map(livro -> {
                    LivroResponseDTO dto = mapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    service.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<LivroResponseDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,

            @RequestParam(value = "titulo", required = false)
            String titulo,

            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,

            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,

            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao,

            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,

            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
    ) {
        Page<Livro> pageResultado = service.pesquisa(
                isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);

        Page<LivroResponseDTO> resultDto = pageResultado.map(mapper::toDTO);

        return ResponseEntity.ok(resultDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id, @RequestBody @Valid LivroDTO livroDto) {
        return service
                .obterPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro livroAux = mapper.toEntity(livroDto);

                    livro.setIsbn(livroAux.getIsbn());
                    livro.setTitulo(livroAux.getTitulo());
                    livro.setGenero(livroAux.getGenero());
                    livro.setDataPublicacao(livroAux.getDataPublicacao());
                    livro.setPreco(livroAux.getPreco());
                    livro.setAutor(livroAux.getAutor());

                    service.atualizar(livro);

                    return ResponseEntity.noContent().build();
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
