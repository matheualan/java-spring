package br.com.relembrar.Relembrando.CRUD.Java.Spring.repository;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
