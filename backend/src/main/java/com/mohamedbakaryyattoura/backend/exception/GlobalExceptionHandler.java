package com.mohamedbakaryyattoura.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gère les erreurs de validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, Object> erreurs = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                erreurs.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("statut", HttpStatus.BAD_REQUEST.value());
        response.put("erreurs", erreurs);
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(response);
    }

    // Gère les RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(
            RuntimeException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("statut", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(response);
    }

    // Gère toutes les autres erreurs
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("statut", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "Erreur interne du serveur");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.internalServerError().body(response);
    }
}