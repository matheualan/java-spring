package br.com.remember.client.service;

import br.com.remember.client.dto.ClientRequest;
import br.com.remember.client.dto.ClientResponse;
import br.com.remember.client.exceptions.ClientNotFoundException;
import br.com.remember.client.mapper.ClientMapper;
import br.com.remember.client.model.Client;
import br.com.remember.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientMapper mapper;

    @InjectMocks
    private ClientService service;

    private Client client;
    private ClientRequest request;
    private ClientResponse response;

    @BeforeEach
    void setup() {
        client = new Client("Matheus", "matheus@email.com", "encoded");
        request = new ClientRequest("Matheus", "matheus@email.com", "123");
        response = new ClientResponse(1L, "Matheus", "matheus@email.com", Instant.now());
    }

    // ================= CREATE =================

    @Test
    void shouldCreateClient() {
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(repository.save(any(Client.class))).thenReturn(client);
        when(mapper.toResponse(client)).thenReturn(response);

        ClientResponse result = service.createClient(request);

        assertNotNull(result);
        verify(repository).save(any(Client.class));
        verify(mapper).toResponse(client);
    }

    // ================= CREATE MULTIPLE =================

    @Test
    void shouldCreateMultipleClients() {
        List<ClientRequest> requests = List.of(request);

        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(mapper.toResponse(any(Client.class))).thenReturn(response);

        List<ClientResponse> result = service.createMultipleClients(requests);

        assertEquals(1, result.size());
        verify(repository).saveAll(anyList());
    }

    @Test
    void shouldThrowWhenPasswordIsNull() {
        ClientRequest invalid = new ClientRequest("Matheus", "email", null);

        assertThrows(IllegalArgumentException.class,
                () -> service.createMultipleClients(List.of(invalid)));
    }

    @Test
    void shouldThrowWhenPasswordIsBlank() {
        ClientRequest invalid = new ClientRequest("Matheus", "email", "");

        assertThrows(IllegalArgumentException.class,
                () -> service.createMultipleClients(List.of(invalid)));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        ClientRequest invalid = new ClientRequest(null, "email", "123");

        assertThrows(IllegalArgumentException.class,
                () -> service.createMultipleClients(List.of(invalid)));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        ClientRequest invalid = new ClientRequest("", "email", "123");

        assertThrows(IllegalArgumentException.class,
                () -> service.createMultipleClients(List.of(invalid)));
    }

    // ================= GET BY ID =================

    @Test
    void shouldReturnClientWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(client));

        Client result = service.getClientById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getClientById(null));
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,
                () -> service.getClientById(1L));
    }

    // ================= FIND BY ID =================

    @Test
    void shouldFindClientById() {
        when(repository.findById(1L)).thenReturn(Optional.of(client));
        when(mapper.toResponse(client)).thenReturn(response);

        ClientResponse result = service.findClientById(1L);

        assertNotNull(result);
    }

    // ================= LIST =================

    @Test
    void shouldListClients() {
        when(repository.findAll()).thenReturn(List.of(client));
        when(mapper.toResponse(client)).thenReturn(response);

        List<ClientResponse> result = service.listClientResponse();

        assertEquals(1, result.size());
    }

    // ================= DELETE =================

    @Test
    void shouldDeleteClient() {
        service.deleteClientById(1L);

        verify(repository).deleteById(1L);
    }

    // ================= UPDATE =================

    @Test
    void shouldUpdateClientWithoutPassword() {
        ClientRequest update = new ClientRequest("Novo", "novo@email.com", null);

        when(repository.findById(1L)).thenReturn(Optional.of(client));
        when(mapper.toResponse(client)).thenReturn(response);

        ClientResponse result = service.updatedClientById(1L, update);

        assertNotNull(result);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldUpdateClientWithPassword() {
        ClientRequest update = new ClientRequest("Novo", "novo@email.com", "123");

        when(repository.findById(1L)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(mapper.toResponse(client)).thenReturn(response);

        ClientResponse result = service.updatedClientById(1L, update);

        assertNotNull(result);
        verify(passwordEncoder).encode("123");
    }

    // ================= AUX METHODS =================

    @Test
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(client));

        List<Client> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindAllRecords() {
        when(repository.findAll()).thenReturn(List.of(client));

        List<ClientRequest> result = service.findAllRecords();

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindAllDto() {
        when(repository.findAll()).thenReturn(List.of(client));

        List<ClientRequest> result = service.findAllDto();

        assertEquals(1, result.size());
    }
}