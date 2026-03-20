package br.com.relembrar.Relembrando.CRUD.Java.Spring.controller;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.ClientPostDTO;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.model.Client;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<Client>> findAll() {
        return ResponseEntity.ok().body(clientService.findAll());
    }

    @GetMapping(path = "/list-record")
    public ResponseEntity<List<ClientPostDTO>> findAllRecords() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllRecords());
    }

    @GetMapping(path = "/list-dto")
    public ResponseEntity<List<ClientPostDTO>> findAllDto() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllDto());
    }

    @PostMapping(path = "/save")
    public ResponseEntity<ClientPostDTO> saveProduct(@RequestBody @Valid ClientPostDTO clientPostDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.saveClient(clientPostDto));
    }

}