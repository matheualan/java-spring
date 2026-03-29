package br.com.remember.client.repository;

import br.com.remember.client.dto.ClientResponse;
import br.com.remember.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<ClientResponse> findClientsByName(String name);

}
