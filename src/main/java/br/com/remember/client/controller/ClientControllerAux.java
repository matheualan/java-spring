package br.com.remember.client.controller;

import br.com.remember.client.dto.ClientRequest;
import br.com.remember.client.model.Client;
import br.com.remember.client.service.ClientService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping(path = "/clientTest")
public class ClientControllerAux {

    private final ClientService clientService;

    public ClientControllerAux(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = "/entities")
    public ResponseEntity<List<Client>> findAll() {
        return ResponseEntity.ok().body(clientService.findAll());
    }

    @GetMapping(path = "/list-dto")
    public ResponseEntity<List<ClientRequest>> findAllDto() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllDto());
    }

    @GetMapping(path = "/list-record")
    public ResponseEntity<List<ClientRequest>> findAllRecords() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllRecords());
    }

}