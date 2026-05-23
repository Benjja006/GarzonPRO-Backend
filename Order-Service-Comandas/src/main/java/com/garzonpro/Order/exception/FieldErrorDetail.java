package com.garzonpro.Order.exception;

public record FieldErrorDetail(
        String field,       // Ejemplo: "cantidad" o "precio"
        String message      // Ejemplo: "La cantidad no puede ser negativa"
) {}