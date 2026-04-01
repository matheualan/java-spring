package br.com.remember.client.controller;

import br.com.remember.client.dto.ClientRequest;
import br.com.remember.client.dto.ClientResponse;
// Ajuste este import se sua exception estiver em outro pacote
import br.com.remember.client.exceptions.ClientNotFoundException;
import br.com.remember.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
//import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
//        properties = {
//                "spring.autoconfigure.exclude=" +
//                        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
//                        "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
//                        "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration," +
//                        "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
//        }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    // ===============================
    // CREATE
    // ===============================

    @Test
    @DisplayName("POST /clients/ -> 201 Created")
    void shouldCreateClient() throws Exception {
        ClientRequest request = newValidRequest();
        ClientResponse response = newResponse(1L, "Matheus", "matheus@email.com");

        Mockito.when(clientService.createClient(any(ClientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Matheus"))
                .andExpect(jsonPath("$.email").value("matheus@email.com"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("POST /clients/ -> 400 Validation Error")
    void shouldReturn400WhenCreateBodyIsInvalid() throws Exception {
        ClientRequest invalid = new ClientRequest(
                "Ma",
                "email-invalido",
                "123"
        );

        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido, verifique a documentação."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.developerMessage")
                        .value("org.springframework.web.bind.MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.path").value("/clients/"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /clients/saveAll -> 201 Created")
    void shouldCreateMultipleClients() throws Exception {
        List<ClientRequest> requests = List.of(
                newValidRequest(),
                new ClientRequest("João Silva", "joao@email.com", "12345678")
        );

        List<ClientResponse> responses = List.of(
                newResponse(1L, "Matheus", "matheus@email.com"),
                newResponse(2L, "João Silva", "joao@email.com")
        );

        Mockito.when(clientService.createMultipleClients(anyList()))
                .thenReturn(responses);

        mockMvc.perform(post("/clients/saveAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Matheus"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("joao@email.com"));
    }

    @Test
    @DisplayName("POST /clients/saveAll -> 400 Validation Error")
    void shouldReturn400WhenCreateMultipleClientsBodyIsInvalid() throws Exception {
        List<ClientRequest> invalidRequests = List.of(
                new ClientRequest("Ma", "email-invalido", "123")
        );

        mockMvc.perform(post("/clients/saveAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequests)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido, verifique a documentação."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.developerMessage")
                        .value("org.springframework.web.bind.MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.path").value("/clients/saveAll"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ===============================
    // FIND BY ID
    // ===============================

    @Test
    @DisplayName("GET /clients/byId -> 200 OK")
    void shouldFindClientById() throws Exception {
        ClientResponse response = newResponse(1L, "Matheus", "matheus@email.com");

        Mockito.when(clientService.findClientById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/clients/byId").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Matheus"))
                .andExpect(jsonPath("$.email").value("matheus@email.com"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("GET /clients/byId -> 404 Client Not Found")
    void shouldReturn404WhenClientIsNotFoundById() throws Exception {
        Mockito.when(clientService.findClientById(99L))
                .thenThrow(new ClientNotFoundException("Cliente não existe"));

        mockMvc.perform(get("/clients/byId").param("id", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cliente não encontrado, verifique os dados."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Cliente não existe"))
                .andExpect(jsonPath("$.developerMessage")
                        .value("br.com.remember.client.exception.ClientNotFoundException"))
                .andExpect(jsonPath("$.path").value("/clients/byId"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /clients/byId -> 400 IllegalArgumentException")
    void shouldReturn400WhenFindByIdReceivesBusinessInvalidArgument() throws Exception {
        Mockito.when(clientService.findClientById(0L))
                .thenThrow(new IllegalArgumentException("ID inválido"));

        mockMvc.perform(get("/clients/byId").param("id", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("ID inválido"))
                .andExpect(jsonPath("$.developerMessage")
                        .value("java.lang.IllegalArgumentException"))
                .andExpect(jsonPath("$.path").value("/clients/byId"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ===============================
    // LIST
    // ===============================

    @Test
    @DisplayName("GET /clients/ -> 200 OK with 2 items")
    void shouldListClients() throws Exception {
        List<ClientResponse> list = List.of(
                newResponse(1L, "A", "a@email.com"),
                newResponse(2L, "B", "b@email.com")
        );

        Mockito.when(clientService.listClientResponse())
                .thenReturn(list);

        mockMvc.perform(get("/clients/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /clients/ -> 200 OK with empty list")
    void shouldReturnEmptyListWhenNoClientsExist() throws Exception {
        Mockito.when(clientService.listClientResponse())
                .thenReturn(List.of());

        mockMvc.perform(get("/clients/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===============================
    // DELETE
    // ===============================

    @Test
    @DisplayName("DELETE /clients/ -> 204 No Content")
    void shouldDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClientById(1L);

        mockMvc.perform(delete("/clients/").param("id", "1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("DELETE /clients/ -> 404 Client Not Found")
    void shouldReturn404WhenDeleteTargetDoesNotExist() throws Exception {
        doThrow(new ClientNotFoundException("Cliente não existe"))
                .when(clientService).deleteClientById(99L);

        mockMvc.perform(delete("/clients/").param("id", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cliente não encontrado, verifique os dados."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Cliente não existe"))
                .andExpect(jsonPath("$.path").value("/clients/"));
    }

    @Test
    @DisplayName("DELETE /clients/ -> 400 IllegalArgumentException")
    void shouldReturn400WhenDeleteReceivesBusinessInvalidArgument() throws Exception {
        doThrow(new IllegalArgumentException("ID inválido"))
                .when(clientService).deleteClientById(0L);

        mockMvc.perform(delete("/clients/").param("id", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("ID inválido"))
                .andExpect(jsonPath("$.path").value("/clients/"));
    }

    // ===============================
    // UPDATE
    // ===============================

    @Test
    @DisplayName("PUT /clients/ -> 200 OK")
    void shouldUpdateClient() throws Exception {
        ClientRequest request = new ClientRequest(
                "Novo Nome",
                "novo@email.com",
                "12345678"
        );

        ClientResponse response = newResponse(1L, "Novo Nome", "novo@email.com");

        Mockito.when(clientService.updatedClientById(eq(1L), any(ClientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/clients/")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Novo Nome"))
                .andExpect(jsonPath("$.email").value("novo@email.com"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("PUT /clients/ -> 400 Validation Error")
    void shouldReturn400WhenUpdateBodyIsInvalid() throws Exception {
        ClientRequest invalid = new ClientRequest(
                "Ma",
                "email-invalido",
                "123"
        );

        mockMvc.perform(put("/clients/")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido, verifique a documentação."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.developerMessage")
                        .value("org.springframework.web.bind.MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.path").value("/clients/"));
    }

    @Test
    @DisplayName("PUT /clients/ -> 404 Client Not Found")
    void shouldReturn404WhenUpdatingNonExistingClient() throws Exception {
        ClientRequest request = newValidRequest();

        Mockito.when(clientService.updatedClientById(eq(1L), any(ClientRequest.class)))
                .thenThrow(new ClientNotFoundException("Cliente não existe"));

        mockMvc.perform(put("/clients/")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cliente não encontrado, verifique os dados."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Cliente não existe"))
                .andExpect(jsonPath("$.path").value("/clients/"));
    }

    @Test
    @DisplayName("PUT /clients/ -> 400 IllegalArgumentException")
    void shouldReturn400WhenUpdateReceivesBusinessInvalidArgument() throws Exception {
        ClientRequest request = newValidRequest();

        Mockito.when(clientService.updatedClientById(eq(0L), any(ClientRequest.class)))
                .thenThrow(new IllegalArgumentException("ID inválido"));

        mockMvc.perform(put("/clients/")
                        .param("id", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("ID inválido"))
                .andExpect(jsonPath("$.path").value("/clients/"));
    }

    @Test
    @DisplayName("PUT /clients/ -> 500 Generic Exception")
    void shouldReturn500WhenUnexpectedExceptionHappensOnUpdate() throws Exception {
        ClientRequest request = newValidRequest();

        Mockito.when(clientService.updatedClientById(eq(1L), any(ClientRequest.class)))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(put("/clients/")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Ops! Uma exceção foi lançada")))
                .andExpect(content().string(containsString("java.lang.RuntimeException")));
    }

    // ===============================
    // HELPERS
    // ===============================

    private ClientRequest newValidRequest() {
        return new ClientRequest(
                "Matheus",
                "matheus@email.com",
                "12345678"
        );
    }

    private ClientResponse newResponse(Long id, String name, String email) {
        return new ClientResponse(
                id,
                name,
                email,
                Instant.parse("2026-01-10T12:00:00Z")
        );
    }
}