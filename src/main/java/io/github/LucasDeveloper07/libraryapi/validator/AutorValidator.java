package io.github.LucasDeveloper07.libraryapi.validator;

import io.github.LucasDeveloper07.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.LucasDeveloper07.libraryapi.model.Autor;
import io.github.LucasDeveloper07.libraryapi.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutorValidator {

    private final AutorRepository repository;

    public void validar(Autor autor) {
        if (existsAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }
    }

    private boolean existsAutorCadastrado(Autor autor) {
        Optional<Autor> autorOptional = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
        );

        if (autor.getId() == null) {
            return autorOptional.isPresent();
        }

        return !autor.getId().equals(autorOptional.get().getId()) && autorOptional.isPresent();
    }
}
