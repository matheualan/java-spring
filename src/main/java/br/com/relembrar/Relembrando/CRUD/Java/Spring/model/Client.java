package br.com.relembrar.Relembrando.CRUD.Java.Spring.model;

import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.client.ClientRequest;
import br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.client.ClientResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity(name = "tb_clients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "password")
public class Client {

    public Client(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @JsonFormat(
            pattern = "dd/MM/yyyy HH:mm:ss",
            timezone = "America/Sao_Paulo"
    )
    @CreationTimestamp
    private Instant createdAt;

    public ClientRequest toRequestDTO() {
        return new ClientRequest(name, email, password);
    }

    public ClientResponse toResponseDTO() {
        return new ClientResponse(name, email);
    }

}