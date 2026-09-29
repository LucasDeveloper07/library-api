package io.github.LucasDeveloper07.libraryapi.validator;

import io.github.LucasDeveloper07.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.LucasDeveloper07.libraryapi.model.Client;
import io.github.LucasDeveloper07.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientValidator {

    private final ClientRepository repository;

    public void validator(Client client) {
        Client clientIdEncontrado = repository.findByClientId(client.getClientId());

        if (clientIdEncontrado != null) {
            throw new OperacaoNaoPermitidaException("Client já está cadastrado na base!");
        }
    }
}
