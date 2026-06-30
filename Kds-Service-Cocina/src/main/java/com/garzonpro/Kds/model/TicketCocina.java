package com.garzonpro.Kds.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TicketCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    private String estadoGeneral; // Ejemplo: "En Preparación", "Listo"
}