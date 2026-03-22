package br.com.relembrar.Relembrando.CRUD.Java.Spring.service;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.client.ClientRequest;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.client.ClientResponse;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.exceptions.ClientNotFoundException;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.model.Client;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional()
    public ClientRequest saveClient(ClientRequest clientRequest) {
        Client client = new Client(
                clientRequest.name(),
                clientRequest.email(),
                clientRequest.password()
        );
        clientRepository.save(client);
        return clientRequest;
    }

    @Transactional
    public List<ClientRequest> saveMultipleClients(List<ClientRequest> listClientsDTO) {
        List<Client> clients = new ArrayList<>();
        for (ClientRequest clientDto : listClientsDTO) {
            clients.add(new Client(clientDto.name(), clientDto.email(), clientDto.password()));
        }
        clientRepository.saveAll(clients);
        return listClientsDTO;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<ClientResponse> findClientById(Long id) {
        if (id == null) return Optional.empty();

        Client client = clientRepository.findById(id).get();

        return Optional.of(new ClientResponse(client.getName(),
                client.getEmail()));
    }

    public Client getClientById(Long id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com o ID: " + id));
    }

//    So para saber que existe essa forma
    public List<ClientRequest> findAllRecords() {
        return findAll()
                .stream()
                .map(p -> new ClientRequest(
                        p.getName(),
                        p.getEmail(),
                        p.getPassword()
                ))
                .toList();
    }

    public List<ClientRequest> findAllDto() {
        return findAll()
                .stream()
                .map(Client::toRequestDTO)
                .toList();
    }

    public List<ClientResponse> listClientResponse() {
        return clientRepository.findAll()
                .stream()
                .map(Client::toResponseDTO)
                .toList();
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

}