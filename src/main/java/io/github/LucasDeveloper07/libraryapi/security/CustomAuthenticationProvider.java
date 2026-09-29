package io.github.LucasDeveloper07.libraryapi.security;

import io.github.LucasDeveloper07.libraryapi.model.Usuario;
import io.github.LucasDeveloper07.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginDig = authentication.getName();
        String senhaDig = authentication.getCredentials().toString();

        Usuario usuarioEncontrado = usuarioService.obterPorLogin(loginDig);

        if (usuarioEncontrado == null) {
            throw getUsernameNotFoundException();
        }

        String senhaCrip = usuarioEncontrado.getSenha();

        boolean senhaEquals = encoder.matches(senhaDig, senhaCrip);

        if (senhaEquals) {
            return new CustomAuthentication(usuarioEncontrado);
        }

        throw getUsernameNotFoundException();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    private UsernameNotFoundException getUsernameNotFoundException() {
        return new UsernameNotFoundException("Usuário e/ou senha incorretos!");
    }
}
