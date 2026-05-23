package com.garzonpro.Reporting.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        field -> field.getField(),
                        field -> field.getDefaultMessage()
                ));

        log.warn("Validación fallida en Reporting-Service: {}", errores);

        // CORRECCIÓN: Signo "=" agregado
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errores", errores);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CierreNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(CierreNotFoundException ex) {
        log.warn("Cierre no encontrado: {}", ex.getMessage());

        // CORRECCIÓN: Signo "=" agregado
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(CierreDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicado(CierreDuplicadoException ex) {
        log.warn("Cierre duplicado: {}", ex.getMessage());

        // CORRECCIÓN: Signo "=" agregado
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        log.error("Error interno en Reporting-Service: {}", ex.getMessage(), ex);

        // CORRECCIÓN: Signo "=" agregado
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error interno del servidor. Contacte al administrador.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}