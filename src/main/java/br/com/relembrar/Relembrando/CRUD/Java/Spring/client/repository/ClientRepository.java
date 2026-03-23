package br.com.relembrar.Relembrando.CRUD.Java.Spring.client.repository;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
