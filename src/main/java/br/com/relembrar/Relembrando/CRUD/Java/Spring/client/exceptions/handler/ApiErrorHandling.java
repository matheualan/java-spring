package br.com.relembrar.Relembrando.CRUD.Java.Spring.client.exceptions.handler;

import java.time.LocalDateTime;

public record ApiErrorHandling(int status,
                               String error,
                               String message,
                               String path,
                               LocalDateTime timestamp) {
}