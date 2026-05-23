package com.garzonpro.Status.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del garzón destino es obligatorio")
    @Positive(message = "El ID del garzón debe ser un número positivo")
    private Long idGarzonDestino;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;
}