package io.github.LucasDeveloper07.libraryapi.service;

import io.github.LucasDeveloper07.libraryapi.model.Usuario;
import io.github.LucasDeveloper07.libraryapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public void salvar(Usuario usuario) {
        String senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));

        repository.save(usuario);
    }

    public Usuario obterPorLogin(String login) {
        return repository.findByLogin(login);
    }
}
