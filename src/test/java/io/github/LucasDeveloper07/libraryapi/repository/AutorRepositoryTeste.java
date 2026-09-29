package io.github.LucasDeveloper07.libraryapi.repository;

import io.github.LucasDeveloper07.libraryapi.model.Autor;
import io.github.LucasDeveloper07.libraryapi.model.GeneroLivro;
import io.github.LucasDeveloper07.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTeste {

    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();

        autor.setNome("Vitor Andrade Coelho");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1996, 7, 29));

        var autorSalvo = repository.save(autor);
        System.out.println("Autor salvo: " + autorSalvo);
    }

    @Test
    public void atulizarTest() {
        var id = UUID.fromString("3edb28cc-25aa-42ac-8ed4-e9164e8483af");

        Optional<Autor> buscaAutor = repository.findById(id);

        if (buscaAutor.isPresent()) {
            Autor autorEncontrado = buscaAutor.get();

            System.out.println("Dados do autor: " + autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(2000, 7, 2));

            repository.save(autorEncontrado);
        }
    }

    @Test
    public void listarTest() {
        List<Autor> listaAutores = repository.findAll();
        listaAutores.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Contagem de autores: " + repository.count());
    }

    @Test
    public void deletePorIdTest() {
        var id = UUID.fromString("3edb28cc-25aa-42ac-8ed4-e9164e8483af");
        repository.deleteById(id);
    }

    @Test
    public void deleteTest() {
        var id = UUID.fromString("30750727-f782-40e7-b6c1-ba8f8c91db1d");

        Optional<Autor> autorEncontrado = repository.findById(id);

        autorEncontrado.ifPresent(autor -> repository.delete(autor));
    }

    @Test
    void salvarAutorComLivrosTest() {
        Autor autor = new Autor();

        autor.setNome("Antonio Ribeiro");
        autor.setNacionalidade("Americano");
        autor.setDataNascimento(LocalDate.of(1965, 9, 3));

        Livro livro = new Livro();

        livro.setIsbn("24865-20412");
        livro.setPreco(BigDecimal.valueOf(250));
        livro.setGenero(GeneroLivro.ROMANCE);
        livro.setTitulo("Livro Romance");
        livro.setDataPublicacao(LocalDate.of(2000, 5, 24));
        livro.setAutor(autor);

        Livro livro2 = new Livro();

        livro2.setIsbn("34567-09876");
        livro2.setPreco(BigDecimal.valueOf(400));
        livro2.setGenero(GeneroLivro.MISTERIO);
        livro2.setTitulo("O grande roubo");
        livro2.setDataPublicacao(LocalDate.of(2005, 9, 23));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        repository.save(autor);

        livroRepository.saveAll(autor.getLivros());
    }

    @Test
    void listarLivrosAutorTest() {
        UUID id = UUID.fromString("5aa8962d-c877-4cf7-9a46-d23e2394cf13");
        var autor = repository.findById(id).get();

        List<Livro> livrosLista = livroRepository.findByAutor(autor);

        autor.setLivros(livrosLista);
        autor.getLivros().forEach(System.out::println);
    }
}
