package io.github.LucasDeveloper07.libraryapi.repository;

import io.github.LucasDeveloper07.libraryapi.model.Autor;
import io.github.LucasDeveloper07.libraryapi.model.GeneroLivro;
import io.github.LucasDeveloper07.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @see LivroRepositoryTest
 */
public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    List<Livro> findByAutor(Autor autor);

    Optional<Livro> findByIsbn(String isbn);

    @Query("SELECT l FROM Livro AS l ORDER BY l.titulo, l.preco")
    List<Livro> listarTodosOrdenadoPorTituloAndPreco();

    @Query("SELECT a FROM Livro l JOIN l.autor a")
    List<Autor> listarAutoresDosLivros();

    @Query(""" 
        SELECT l.genero
        FROM Livro l
        JOIN l.autor a
        WHERE a.nacionalidade = 'Brasileiro'
        ORDER BY l.genero
    """)
    List<String> listarGenerosAutoresBr();

    @Query("SELECT l FROM Livro l WHERE l.genero = :genero")
    List<Livro> findByGenero(@Param("genero") GeneroLivro generoLivro);

    @Modifying
    @Transactional
    @Query("DELETE FROM Livro WHERE genero = ?1")
    void deleteByGenero(GeneroLivro generoLivro);

    @Modifying
    @Transactional
    @Query("UPDATE Livro SET dataPublicacao = ?1")
    void updateDataPublicacao(LocalDate novaData);

    boolean existsByAutor(Autor autor);
}
