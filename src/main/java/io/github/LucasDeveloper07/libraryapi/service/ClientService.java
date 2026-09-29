package io.github.LucasDeveloper07.libraryapi.service;

import io.github.LucasDeveloper07.libraryapi.model.Client;
import io.github.LucasDeveloper07.libraryapi.repository.ClientRepository;
import io.github.LucasDeveloper07.libraryapi.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientValidator validator;
    private final PasswordEncoder encoder;

    public Client salvar(Client client) {
        validator.validator(client);

        var senhaCrip = encoder.encode(client.getClientSecret());
        client.setClientSecret(senhaCrip);

        return repository.save(client);
    }

    public Client obterPorClientID(String clientId) {
        return repository.findByClientId(clientId);
    }
}
