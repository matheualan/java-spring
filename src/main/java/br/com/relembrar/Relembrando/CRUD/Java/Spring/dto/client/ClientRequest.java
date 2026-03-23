package br.com.relembrar.Relembrando.CRUD.Java.Spring.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank(message = "Nome não pode ser vazio.")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres.")
        String name,

        @NotBlank(message = "Email não pode ser vazio.")
        @Email(message = "Email inválido.")
        String email,

        @NotBlank(message = "Senha não pode ser vazia.")
        @Size(min = 8, max = 255, message = "Senha deve ter entre 8 e 20 caracteres.")
        String password) {
}