package com.garzonpro.Status.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notificacion")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @Column(name = "id_garzon_destino", nullable = false)
    private Long idGarzonDestino;

    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "leido", nullable = false)
    private boolean leido = false;
}