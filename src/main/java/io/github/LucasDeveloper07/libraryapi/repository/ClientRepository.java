package io.github.LucasDeveloper07.libraryapi.repository;

import io.github.LucasDeveloper07.libraryapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByClientId(String clientId);
}
