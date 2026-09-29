package io.github.LucasDeveloper07.libraryapi.security;

import io.github.LucasDeveloper07.libraryapi.model.Usuario;
import io.github.LucasDeveloper07.libraryapi.repository.UsuarioRepository;
import io.github.LucasDeveloper07.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginSocialSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String SENHA_PADRAO = "321";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService service;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();

        String email = principal.getAttribute("email");

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setLogin(getTratarEmail(email));
            usuario.setSenha(SENHA_PADRAO);
            usuario.setRoles(List.of("OPERADOR"));

            service.salvar(usuario);
        }

        authentication = new CustomAuthentication(usuario);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String getTratarEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
