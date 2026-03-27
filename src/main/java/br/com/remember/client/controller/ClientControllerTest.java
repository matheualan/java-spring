package br.com.remember.client.controller;

import br.com.remember.client.service.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/clientTest")
public class ClientControllerTest {

    private final ClientService clientService;

    public ClientControllerTest(ClientService clientService) {
        this.clientService = clientService;
    }



}
