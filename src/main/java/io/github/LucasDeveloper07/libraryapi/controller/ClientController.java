package io.github.LucasDeveloper07.libraryapi.controller;

import io.github.LucasDeveloper07.libraryapi.controller.dto.ClientDTO;
import io.github.LucasDeveloper07.libraryapi.controller.mappers.ClientMapper;
import io.github.LucasDeveloper07.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
@Tag(name = "Clients")
public class ClientController implements GenericController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso!")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid ClientDTO clientDto) {
        var clientEntity = mapper.toEntity(clientDto);
        service.salvar(clientEntity);

        URI location = generatedHeaderLocation(clientEntity.getId());

        return ResponseEntity.created(location).build();
    }
}
