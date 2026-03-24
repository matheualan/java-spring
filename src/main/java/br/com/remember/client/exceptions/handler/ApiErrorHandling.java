package br.com.remember.client.exceptions.handler;

import java.time.LocalDateTime;

public record ApiErrorHandling(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp) {
}