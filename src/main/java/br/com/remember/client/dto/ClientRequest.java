package br.com.remember.client.dto;

import jakarta.validation.constraints.*;

public record ClientRequest(
        @NotBlank(message = "Nome não pode ser vazio.")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres.")
        String name,

        @NotBlank(message = "Email não pode ser vazio.")
        @Email(message = "Email inválido.")
        String email,

        @NotBlank(message = "Senha deve ter entre 8 a 72 caracteres.")
        @Size(min = 8, max = 72, message = "Senha deve ter entre 8 a 72 caracteres.")
        String password) {
}