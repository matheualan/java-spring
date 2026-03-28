package br.com.remember.client.controller;

import br.com.remember.client.dto.client.ClientRequest;
import br.com.remember.client.dto.client.ClientResponse;
import br.com.remember.client.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(path = "/")
    public ResponseEntity<ClientResponse> saveProduct(@RequestBody @Valid ClientRequest clientRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientRequest));
    }

    @PostMapping(path = "/saveAll")
    public ResponseEntity<List<ClientResponse>> saveMultipleClients(@RequestBody @Valid List<ClientRequest> listClients) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createMultipleClients(listClients));
    }

    @GetMapping(path = "/byId")
    public ResponseEntity<ClientResponse> findClientById(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findClientById(id));
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<ClientResponse>> listClientResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.listClientResponse());
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