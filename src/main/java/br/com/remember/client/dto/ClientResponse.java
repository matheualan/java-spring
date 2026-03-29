package br.com.remember.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record ClientResponse(
        Long id,
        String name,
        String email,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        Instant createdAt) {
}