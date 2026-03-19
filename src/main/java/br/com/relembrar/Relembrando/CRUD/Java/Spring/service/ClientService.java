package br.com.relembrar.Relembrando.CRUD.Java.Spring.service;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.ClientDTO;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.model.Client;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public List<ClientDTO> findAllRecords() {
        return findAll()
                .stream()
                .map(p -> new ClientDTO(
                        p.getName(),
                        p.getEmail(),
                        p.getPassword()
                ))
                .toList();
    }

    public List<ClientDTO> findAllDto() {
        return findAll()
                .stream()
                .map(Client::toDTO)
                .toList();
    }

    @Transactional()
    public ClientDTO saveClient(ClientDTO clientDto) {
        Client client = new Client(
                clientDto.name(),
                clientDto.email(),
                clientDto.password()
        );
        clientRepository.save(client);
        return clientDto;
    }

}