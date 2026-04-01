package br.com.remember.client.controller;

import br.com.remember.client.model.Client;
import br.com.remember.client.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    private static Long createdClientId;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
    }

    // =============================
    // CREATE SUCCESS
    // =============================
    @Test
    @Order(1)
    void shouldCreateClientSuccessfully() throws Exception {

        String json = """
                {
                  "name": "Matheus Alan",
                  "email": "matheus@email.com",
                  "password": "12345678"
                }
                """;

        var result = mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Matheus Alan"))
                .andExpect(jsonPath("$.email").value("matheus@email.com"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();

        List<Client> clients = clientRepository.findAll();
        Assertions.assertEquals(1, clients.size());
        createdClientId = clients.get(0).getId();
    }

    // =============================
    // CREATE VALIDATION ERROR
    // =============================
    @Test
    void shouldReturn400WhenInvalidData() throws Exception {

        String json = """
                {
                  "name": "Ma",
                  "email": "email-invalido",
                  "password": "123"
                }
                """;

        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido, verifique a documentação."))
                .andExpect(jsonPath("$.status").value(400));
    }

    // =============================
    // FIND BY ID SUCCESS
    // =============================
    @Test
    void shouldFindClientById() throws Exception {

        Client client = clientRepository.save(
                new Client("João Silva", "joao@email.com", "12345678")
        );

        mockMvc.perform(get("/clients/byId")
                        .param("id", client.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(client.getId()))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    // =============================
    // FIND BY ID NOT FOUND
    // =============================
    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {

        mockMvc.perform(get("/clients/byId")
                        .param("id", "999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cliente não encontrado, verifique os dados."))
                .andExpect(jsonPath("$.status").value(404));
    }

    // =============================
    // LIST ALL
    // =============================
    @Test
    void shouldListAllClients() throws Exception {

        clientRepository.save(new Client("A", "a@email.com", "12345678"));
        clientRepository.save(new Client("B", "b@email.com", "12345678"));

        mockMvc.perform(get("/clients/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // =============================
    // UPDATE SUCCESS
    // =============================
    @Test
    void shouldUpdateClientSuccessfully() throws Exception {

        Client client = clientRepository.save(
                new Client("Carlos", "carlos@email.com", "12345678")
        );

        String json = """
                {
                  "name": "Carlos Updated",
                  "email": "carlos@email.com",
                  "password": "12345678"
                }
                """;

        mockMvc.perform(put("/clients/")
                        .param("id", client.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos Updated"));
    }

    // =============================
    // DELETE SUCCESS
    // =============================
    @Test
    void shouldDeleteClientSuccessfully() throws Exception {

        Client client = clientRepository.save(
                new Client("Maria", "maria@email.com", "12345678")
        );

        mockMvc.perform(delete("/clients/")
                        .param("id", client.getId().toString()))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(clientRepository.findById(client.getId()).isPresent());
    }

    // =============================
    // DELETE NOT FOUND
    // =============================
    @Test
    void shouldReturn404WhenDeletingNonExistingClient() throws Exception {

        mockMvc.perform(delete("/clients/")
                        .param("id", "999"))
                .andExpect(status().isNotFound());
    }
}
