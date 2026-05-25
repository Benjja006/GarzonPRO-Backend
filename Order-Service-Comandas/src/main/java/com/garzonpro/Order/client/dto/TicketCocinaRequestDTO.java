package com.garzonpro.Order.client.dto;

import lombok.Data;

/**
 * DTO que Order-Service envía a KDS-Service
 * para notificar que hay un nuevo pedido para cocina.
 */
@Data
public class TicketCocinaRequestDTO {

    private Long idPedido;
    private Long idMesa;
    private String nivelAlerta; // NORMAL, URGENTE
}