package br.com.remember.client.exceptions.handler;

import br.com.remember.client.exceptions.ClientNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
//public class RestExceptionHandler {

//    @PostConstruct
//    public void init() {
//        System.out.println("RestExceptionHandler carregado");
//    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleClientNotFoundException(ClientNotFoundException exception) {
        System.out.println("ENTROU NO HANDLER");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDetails.builder()
                        .title("Cliente não encontrado, verifique os dados.")
                        .status(HttpStatus.NOT_FOUND.value())
                        .detail(exception.getMessage())
                        .developerMessage(exception.getClass().toString())
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDetails> handleIllegalArgument(
            IllegalArgumentException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDetails.builder()
                        .title("Argumento inválido")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .detail(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(500).body("Peguei a exceção: " + ex.getClass().getName());
    }

}