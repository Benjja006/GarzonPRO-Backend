package com.garzonpro.Kds.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_cocina")
@Data
public class ItemCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemCocina;

    private String nombrePlato;
    private Integer cantidad;
    private String estado = "EN_COLA";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket")
    private TicketCocina ticket;
}