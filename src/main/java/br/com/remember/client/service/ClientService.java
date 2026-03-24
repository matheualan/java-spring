package br.com.remember.client.service;

import br.com.remember.client.dto.client.ClientRequest;
import br.com.remember.client.dto.client.ClientResponse;
import br.com.remember.client.exceptions.ClientNotFoundException;
import br.com.remember.client.model.Client;
import br.com.remember.client.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional()
    public ClientResponse createClient(ClientRequest clientRequest) {
//        if (clientRequest.password() == null || clientRequest.password().isBlank()) {
//            throw new IllegalArgumentException("Senha é obrigatória para criar um cliente.");
//        }
        Client client = new Client(
                clientRequest.name(),
                clientRequest.email(),
                passwordEncoder.encode(clientRequest.password())
        );
        Client savedClient = clientRepository.save(client);

        return new ClientResponse(savedClient.getId(),
                savedClient.getName(),
                savedClient.getEmail(),
                savedClient.getCreatedAt());
    }

    @Transactional
    public List<ClientResponse> saveMultipleClients(List<ClientRequest> listClientsDTO) {
        List<Client> clients = new ArrayList<>();
        List<ClientResponse> listResponse = new ArrayList<>();

        for (ClientRequest clientDto : listClientsDTO) {
            if (clientDto.password() == null || clientDto.password().isBlank()) {
                throw new IllegalArgumentException("Senha é obrigatória para criar um cliente.");
            }

            clients.add(new Client(
                    clientDto.name(),
                    clientDto.email(),
                    passwordEncoder.encode(clientDto.password())
            ));
        }
        clientRepository.saveAll(clients);

        for (Client client : clients) {
            listResponse.add(new ClientResponse(client.getId(),
                    client.getName(),
                    client.getEmail(),
                    client.getCreatedAt())
            );
        }

        return listResponse;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<ClientResponse> findClientById(Long id) {
        if (id == null) return Optional.empty();

//        Client client = clientRepository.findById(id).get();

        Client client = getClientById(id);

        return Optional.of(new ClientResponse(client.getId(),
                client.getName(),
                client.getEmail(),
                client.getCreatedAt())
        );
    }

    public Client getClientById(Long id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com o ID: " + id + " não encontrado"));
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

    @Transactional
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    @Transactional
    public ClientResponse updatedClientById(Long id, ClientRequest clientDTO) {
//        ClientResponse clientById = findClientById(id).get();
//        String encrypted = "";
//        if (clientRequest.password() != null && !clientRequest.password().isBlank()) {
//            encrypted = passwordEncoder.encode(clientRequest.password());
//        }

        Client client = getClientById(id);

        client.setName(clientDTO.name());
        client.setEmail(clientDTO.email());

        if (clientDTO.password() != null && !clientDTO.password().isBlank()) {
            String encrypted = passwordEncoder.encode(clientDTO.password());
            client.setPassword(encrypted);
        }

        clientRepository.save(client);

        return new ClientResponse(client.getId(),
                client.getName(),
                client.getEmail(),
                client.getCreatedAt());
    }

}