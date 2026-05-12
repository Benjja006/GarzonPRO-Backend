package com.garzonpro.Status.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;
    private Long idGarzonDestino; // A quién le avisamos
    private String mensaje;       // Ejemplo: "Plato de Mesa 5 Listo"
    private boolean leido = false;
}