package br.com.remember.client.controller;

import br.com.remember.client.dto.client.ClientRequest;
import br.com.remember.client.dto.client.ClientResponse;
import br.com.remember.client.exceptions.ClientNotFoundException;
import br.com.remember.client.model.Client;
import br.com.remember.client.service.ClientService;
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

    @GetMapping("/teste")
    public String teste() {
        throw new ClientNotFoundException("Teste direto no controller");
    }

    @PostMapping(path = "/")
    public ResponseEntity<ClientResponse> saveProduct(@RequestBody @Valid ClientRequest clientRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientRequest));
    }

    @PostMapping(path = "/multiple")
    public ResponseEntity<List<ClientResponse>> saveMultipleClients(@RequestBody @Valid List<ClientRequest> listClients) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.saveMultipleClients(listClients));
    }

    @GetMapping(path = "/clientById")
    public ResponseEntity<Optional<ClientResponse>> findClientById(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findClientById(id));
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<Client>> findAll() {
        return ResponseEntity.ok().body(clientService.findAll());
    }

    @GetMapping(path = "/listResponse")
    public ResponseEntity<List<ClientResponse>> listClientResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.listClientResponse());
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

    @DeleteMapping(path = "/")
    public ResponseEntity<Void> deleteClientById(@RequestParam Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/")
    public ResponseEntity<ClientResponse> updatedClientById(@RequestParam Long id,
                                                            @RequestBody @Valid ClientRequest clientDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updatedClientById(id, clientDTO));
    }

}