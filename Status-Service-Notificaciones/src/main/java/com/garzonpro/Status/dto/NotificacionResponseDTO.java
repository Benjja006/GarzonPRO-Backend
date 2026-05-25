package com.garzonpro.Status.dto;

import lombok.Data;

@Data
public class NotificacionResponseDTO {

    private Long idNotificacion;
    private Long idGarzonDestino;
    private String mensaje;
    private boolean leido;
}