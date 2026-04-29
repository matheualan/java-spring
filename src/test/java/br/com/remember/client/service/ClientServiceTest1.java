//package br.com.remember.client.service;
//
//import br.com.remember.client.dto.ClientRequest;
//import br.com.remember.client.dto.ClientResponse;
//import br.com.remember.client.exceptions.ClientNotFoundException;
//import br.com.remember.client.model.Client;
//import br.com.remember.client.repository.ClientRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ClientServiceTest1 {
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private ClientService clientService;
//
//    private Client client;
//    private ClientRequest clientRequest;
//
//    @BeforeEach
//    void setup() {
//        client = new Client("Matheus", "matheus@email.com", "encodedPassword");
//        client.setId(1L);
//        client.setCreatedAt(Instant.now());
//
//        clientRequest = new ClientRequest(
//                "Matheus",
//                "matheus@email.com",
//                "123456"
//        );
//    }
//
//    // ===============================
//    // createClient
//    // ===============================
//
//    @Test
//    void shouldCreateClientSuccessfully() {
//
//        // Arrange
//        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
//        when(clientRepository.save(any(Client.class))).thenReturn(client);
//
//        // Act
//        ClientResponse response = clientService.createClient(clientRequest);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("Matheus", response.name());
//        assertEquals("matheus@email.com", response.email());
//
//        verify(passwordEncoder).encode("123456");
//        verify(clientRepository).save(any(Client.class));
//    }
//
//    // ===============================
//    // createMultipleClients
//    // ===============================
//
//    @Test
//    void shouldCreateMultipleClientsSuccessfully() {
//
//        // Arrange
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//
//        List<ClientRequest> list = List.of(clientRequest);
//
//        // Act
//        List<ClientResponse> responses = clientService.createMultipleClients(list);
//
//        // Assert
//        assertEquals(1, responses.size());
//        verify(clientRepository).saveAll(anyList());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordIsNullInMultipleCreation() {
//
//        // Arrange
//        ClientRequest invalidRequest =
//                new ClientRequest("Nome", "email@email.com", null);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> clientService.createMultipleClients(List.of(invalidRequest)));
//
//        verify(clientRepository, never()).saveAll(anyList());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordIsBlankInMultipleCreation() {
//
//        // Arrange
//        ClientRequest invalidRequest =
//                new ClientRequest("Nome", "email@email.com", "");
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> clientService.createMultipleClients(List.of(invalidRequest)));
//
//        verify(clientRepository, never()).saveAll(anyList());
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenCreatingMultipleWithEmptyList() {
//
//        // Arrange
//        when(clientRepository.saveAll(anyList())).thenReturn(List.of());
//
//        // Act
//        List<ClientResponse> responses =
//                clientService.createMultipleClients(List.of());
//
//        // Assert
//        assertTrue(responses.isEmpty());
//    }
//
//    // ===============================
//    // getClientById
//    // ===============================
//
//    @Test
//    void shouldReturnClientWhenIdExists() {
//
//        // Arrange
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        // Act
//        Client result = clientService.getClientById(1L);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenClientNotFound() {
//
//        // Arrange
//        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ClientNotFoundException.class,
//                () -> clientService.getClientById(1L));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenIdIsNull() {
//
//        assertThrows(IllegalArgumentException.class,
//                () -> clientService.getClientById(null));
//    }
//
//    @Test
//    void shouldReturnClientResponseWhenFindingById() {
//
//        // Arrange
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        // Act
//        ClientResponse response = clientService.findClientById(1L);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(client.getEmail(), response.email());
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenNoClientsExist() {
//
//        // Arrange
//        when(clientRepository.findAll()).thenReturn(List.of());
//
//        // Act
//        List<ClientResponse> responses = clientService.listClientResponse();
//
//        // Assert
//        assertTrue(responses.isEmpty());
//    }
//
//    // ===============================
//    // listClientResponse
//    // ===============================
//
//    @Test
//    void shouldReturnListOfClientResponses() {
//
//        // Arrange
//        when(clientRepository.findAll()).thenReturn(List.of(client));
//
//        // Act
//        List<ClientResponse> responses = clientService.listClientResponse();
//
//        // Assert
//        assertEquals(1, responses.size());
//        verify(clientRepository).findAll();
//    }
//
//    // ===============================
//    // deleteClientById
//    // ===============================
//
//    @Test
//    void shouldDeleteClientById() {
//
//        // Act
//        clientService.deleteClientById(1L);
//
//        // Assert
//        verify(clientRepository).deleteById(1L);
//    }
//
//    // ===============================
//    // updatedClientById
//    // ===============================
//
//    @Test
//    void shouldUpdateClientSuccessfully() {
//
//        // Arrange
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
//
//        // Act
//        ClientResponse response =
//                clientService.updatedClientById(1L, clientRequest);
//
//        // Assert
//        assertEquals("Matheus", response.name());
//        verify(passwordEncoder).encode("123456");
//    }
//
//    @Test
//    void shouldUpdateClientWithoutChangingPasswordIfBlank() {
//
//        // Arrange
//        ClientRequest requestWithoutPassword =
//                new ClientRequest("NovoNome", "novo@email.com", "");
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        // Act
//        ClientResponse response =
//                clientService.updatedClientById(1L, requestWithoutPassword);
//
//        // Assert
//        assertEquals("NovoNome", response.name());
//        verify(passwordEncoder, never()).encode(anyString());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUpdatingNonExistingClient() {
//
//        // Arrange
//        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ClientNotFoundException.class,
//                () -> clientService.updatedClientById(1L, clientRequest));
//    }
//
//    @Test
//    void shouldUpdateClientWithoutChangingPasswordWhenPasswordIsNull() {
//
//        // Arrange
//        ClientRequest request =
//                new ClientRequest("NovoNome", "novo@email.com", null);
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        // Act
//        ClientResponse response =
//                clientService.updatedClientById(1L, request);
//
//        // Assert
//        assertEquals("NovoNome", response.name());
//        verify(passwordEncoder, never()).encode(anyString());
//    }
//
//    // ===============================
//    // Métodos de Teste ClientControllerAux
//    // ===============================
//
//    @Test
//    void shouldReturnAllClients() {
//
//        // Arrange
//        when(clientRepository.findAll()).thenReturn(List.of(client));
//
//        // Act
//        List<Client> result = clientService.findAll();
//
//        // Assert
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void shouldReturnAllRecordsAsClientRequest() {
//
//        // Arrange
//        when(clientRepository.findAll()).thenReturn(List.of(client));
//
//        // Act
//        List<ClientRequest> result = clientService.findAllRecords();
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals(client.getEmail(), result.get(0).email());
//    }
//
//    @Test
//    void shouldReturnAllDtos() {
//
//        // Arrange
//        when(clientRepository.findAll()).thenReturn(List.of(client));
//
//        // Act
//        List<ClientRequest> result = clientService.findAllDto();
//
//        // Assert
//        assertEquals(1, result.size());
//    }
//}