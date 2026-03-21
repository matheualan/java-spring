package br.com.relembrar.Relembrando.CRUD.Java.Spring.controller;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.ClientRequest;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.ClientResponse;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.model.Client;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.service.ClientService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(path = "/")
    public ResponseEntity<ClientRequest> saveProduct(@RequestBody @Valid ClientRequest clientRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.saveClient(clientRequest));
    }

    @PostMapping(path = "/all")
    public ResponseEntity<List<ClientRequest>> saveMultipleClients(@RequestBody @Valid List<ClientRequest> listClients) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.saveMultipleClients(listClients));
    }

    @GetMapping(path = "/clientById")
    public ResponseEntity<Optional<ClientResponse>> findClientById(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findClientById(id));
    }

    @GetMapping(path = "/listResponse")
    public ResponseEntity<List<ClientResponse>> listClientResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.listClientResponse());
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<Client>> findAll() {
        return ResponseEntity.ok().body(clientService.findAll());
    }

    @GetMapping(path = "/list-dto")
    public ResponseEntity<List<ClientRequest>> findAllDto() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllDto());
    }

    @Hidden
    @GetMapping(path = "/list-record")
    public ResponseEntity<List<ClientRequest>> findAllRecords() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllRecords());
    }


}