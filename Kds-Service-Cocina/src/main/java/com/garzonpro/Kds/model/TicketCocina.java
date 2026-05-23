package com.garzonpro.Kds.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_cocina")
@Data
public class TicketCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    private Long idPedido;
    private Long idMesa;
    private String estadoGeneral = "PENDIENTE";

    @Column(nullable = false)
    private LocalDateTime horaLlegada = LocalDateTime.now();

    private Integer tiempoPreparacion = 0;

    @Enumerated(EnumType.STRING)
    private EnumAlertaDemora nivelAlertaDemora = EnumAlertaDemora.NORMAL;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCocina> items = new ArrayList<>();
}