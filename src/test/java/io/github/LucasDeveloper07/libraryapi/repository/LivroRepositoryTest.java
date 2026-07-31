package io.github.LucasDeveloper07.libraryapi.repository;

import io.github.LucasDeveloper07.libraryapi.model.Autor;
import io.github.LucasDeveloper07.libraryapi.model.GeneroLivro;
import io.github.LucasDeveloper07.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository repository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest() {
        Livro livro = new Livro();

        livro.setIsbn("99900-82211");
        livro.setPreco(BigDecimal.valueOf(350.90));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setTitulo("Polo norte");
        livro.setDataPublicacao(LocalDate.of(1990, 7, 20));

        Autor autor = autorRepository
                .findById(UUID.fromString("202dc133-26ea-4a5d-9b81-d912164499d7"))
                .orElse(null);

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void salvarCascadeTest() {
        Livro livro = new Livro();

        livro.setIsbn("10783-13332");
        livro.setPreco(BigDecimal.valueOf(129.90));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setTitulo("Sumiço");
        livro.setDataPublicacao(LocalDate.of(2015, 3, 5));

        Autor autor = new Autor();

        autor.setNome("Ronaldo Cardoso");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1990, 9, 21));

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void atualizarAutorLivroTest() {
        UUID id = UUID.fromString("fe8a3c53-37d5-4d1c-861f-874f102fae07");
        var livroParaAtualizar = repository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("2da0656c-7f54-4fcc-ae51-d0f8ac1e86b3");
        Autor autorAtualizado = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(autorAtualizado);

        repository.save(livroParaAtualizar);
    }

    @Test
    void deletarTest() {
        UUID id = UUID.fromString("fe8a3c53-37d5-4d1c-861f-874f102fae07");
        repository.deleteById(id);
    }

    @Test
    void buscarLivroTest() {
        UUID id = UUID.fromString("30b4c4c4-0e1e-4912-8e40-55203cf064a8");
        Livro livro = repository.findById(id).orElse(null);

        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor().getNome());
    }

    @Test
    void listarLivrosComQueryJPQL() {
        var resultado = repository.listarTodosOrdenadoPorTituloAndPreco();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresDosLivros() {
        var resultado = repository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGenerosdeAutoresBr() {
        var resultado = repository.listarGenerosAutoresBr();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParam() {
        var resultado = repository.findByGenero(GeneroLivro.FICCAO);
        resultado.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest() {
        repository.deleteByGenero(GeneroLivro.CIENCIA);
    }

    @Test
    void updateDataPublicacaoTest() {
        repository.updateDataPublicacao(LocalDate.of(2000, 1, 1));
    }


}