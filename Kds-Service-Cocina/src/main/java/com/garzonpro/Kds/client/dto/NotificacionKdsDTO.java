package com.garzonpro.Kds.client.dto;

import lombok.Data;

/**
 * DTO que KDS-Service envía a Status-Service
 * para notificar al garzón que su pedido está listo.
 */
@Data
public class NotificacionKdsDTO {

    private Long idGarzonDestino;
    private String mensaje; // Ejemplo: "Mesa 5 - Plato listo para servir"
}