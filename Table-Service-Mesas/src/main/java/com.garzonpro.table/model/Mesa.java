package com.garzonpro.table.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesas")
@Data
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    private Integer nombreMesa;
    private String estado; // Usa los valores de tu Enum: Libre, Ocupada, Reservada
}