package br.com.relembrar.Relembrando.CRUD.Java.Spring.service;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.ClientPostDTO;
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

    public List<ClientPostDTO> findAllRecords() {
        return findAll()
                .stream()
                .map(p -> new ClientPostDTO(
                        p.getName(),
                        p.getEmail(),
                        p.getPassword()
                ))
                .toList();
    }

    public List<ClientPostDTO> findAllDto() {
        return findAll()
                .stream()
                .map(Client::toDTO)
                .toList();
    }

    @Transactional()
    public ClientPostDTO saveClient(ClientPostDTO clientPostDto) {
        Client client = new Client(
                clientPostDto.name(),
                clientPostDto.email(),
                clientPostDto.password()
        );
        clientRepository.save(client);
        return clientPostDto;
    }

}