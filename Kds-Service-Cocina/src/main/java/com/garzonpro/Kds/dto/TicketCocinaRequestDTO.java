package com.garzonpro.Kds.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketCocinaRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;

    @NotNull(message = "El ID de la mesa es obligatorio")
    private Long idMesa;

    // Aquí puedes agregar más campos si tu frontend los envía al crear el ticket,
    // por ejemplo, una lista de items, pero con el idPedido basta para compilar la base.
    private String estadoGeneral = "PENDIENTE";
}