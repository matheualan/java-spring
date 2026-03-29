//package br.com.remember.client.controller;
//
//import br.com.remember.client.dto.ClientRequest;
//import br.com.remember.client.dto.ClientResponse;
//import br.com.remember.client.exceptions.ClientNotFoundException;
//import br.com.remember.client.service.ClientService;
//import br.com.remember.client.exceptions.RestExceptionHandler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.Instant;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ClientController.class)
//@Import(RestExceptionHandler.class)
//class ClientControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ClientService clientService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ClientRequest buildRequest() {
//        return new ClientRequest("Matheus", "matheus@email.com", "123456");
//    }
//
//    private ClientResponse buildResponse() {
//        return new ClientResponse(1L, "Matheus", "matheus@email.com", Instant.now());
//    }
//
//    // ===============================
//    // CREATE CLIENT
//    // ===============================
//
//    @Test
//    @DisplayName("Should create client successfully")
//    void shouldCreateClient() throws Exception {
//
//        when(clientService.createClient(any())).thenReturn(buildResponse());
//
//        mockMvc.perform(post("/clients/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(buildRequest())))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("Matheus"))
//                .andExpect(jsonPath("$.email").value("matheus@email.com"));
//    }
//
//    @Test
//    @DisplayName("Should return 400 when validation fails on create")
//    void shouldReturn400WhenValidationFails() throws Exception {
//
//        ClientRequest invalid = new ClientRequest("", "", "");
//
//        mockMvc.perform(post("/clients/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalid)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(400));
//    }
//
//    // ===============================
//    // CREATE MULTIPLE
//    // ===============================
//
//    @Test
//    void shouldCreateMultipleClients() throws Exception {
//
//        when(clientService.createMultipleClients(any()))
//                .thenReturn(List.of(buildResponse()));
//
//        mockMvc.perform(post("/clients/saveAll")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(List.of(buildRequest()))))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$[0].name").value("Matheus"));
//    }
//
//    // ===============================
//    // FIND BY ID
//    // ===============================
//
//    @Test
//    void shouldFindClientById() throws Exception {
//
//        when(clientService.findClientById(1L)).thenReturn(buildResponse());
//
//        mockMvc.perform(get("/clients/byId?id=1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("matheus@email.com"));
//    }
//
//    @Test
//    void shouldReturn404WhenClientNotFound() throws Exception {
//
//        when(clientService.findClientById(1L))
//                .thenThrow(new ClientNotFoundException("Cliente não encontrado"));
//
//        mockMvc.perform(get("/clients/byId?id=1"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value(404))
//                .andExpect(jsonPath("$.title").value("Cliente não encontrado, verifique os dados."));
//    }
//
//    // ===============================
//    // LIST ALL
//    // ===============================
//
//    @Test
//    void shouldListClients() throws Exception {
//
//        when(clientService.listClientResponse())
//                .thenReturn(List.of(buildResponse()));
//
//        mockMvc.perform(get("/clients/"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Matheus"));
//    }
//
//    // ===============================
//    // DELETE
//    // ===============================
//
//    @Test
//    void shouldDeleteClient() throws Exception {
//
//        doNothing().when(clientService).deleteClientById(1L);
//
//        mockMvc.perform(delete("/clients/?id=1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void shouldReturn400WhenDeleteWithInvalidArgument() throws Exception {
//
//        when(clientService.deleteClientById(eq(null)))
//                .thenThrow(new IllegalArgumentException("ID inválido"));
//
//        mockMvc.perform(delete("/clients/"))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ===============================
//    // UPDATE
//    // ===============================
//
//    @Test
//    void shouldUpdateClient() throws Exception {
//
//        when(clientService.updatedClientById(eq(1L), any()))
//                .thenReturn(buildResponse());
//
//        mockMvc.perform(put("/clients/?id=1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(buildRequest())))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Matheus"));
//    }
//}