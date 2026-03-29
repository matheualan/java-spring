package br.com.remember.client.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
//public class RestExceptionHandler extends ResponseEntityExceptionHandler {
public class RestExceptionHandler {

//    @PostConstruct
//    public void init() {
//        System.out.println("RestExceptionHandler carregado");
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(500).body("Ops! Uma exceção foi lançada: " + ex.getClass().getName());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleClientNotFoundException(
            ClientNotFoundException exception,
            HttpServletRequest request) {

        String path = request.getRequestURI();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDetails.builder()
                        .title("Cliente não encontrado, verifique os dados.")
                        .status(HttpStatus.NOT_FOUND.value())
                        .detail(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .path(path)
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDetails> handleIllegalArgument(
            IllegalArgumentException exception,
            WebRequest webRequest) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        String path = servletWebRequest.getRequest().getRequestURI();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDetails.builder()
                        .title("Argumento inválido")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .detail(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .path(path)
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String path = request.getRequestURI();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDetails.builder()
                        .title("Argumento inválido, verifique a documentação.")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .detail(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .path(path)
                        .timestamp(Instant.now())
                        .build()
        );
    }

}