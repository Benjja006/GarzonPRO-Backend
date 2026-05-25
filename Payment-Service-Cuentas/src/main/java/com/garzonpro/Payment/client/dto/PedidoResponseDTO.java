package com.garzonpro.Payment.client.dto;

import lombok.Data;

/**
 * DTO con los datos del pedido que Payment-Service
 * recibe desde Order-Service para validar antes de procesar el pago.
 */
@Data
public class PedidoResponseDTO {

    private Long idPedido;
    private Long idMesa;
    private String estadoGeneral; // ABIERTO, CERRADO, CANCELADO
    private Double totalParcial;
}