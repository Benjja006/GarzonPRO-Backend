package com.garzonpro.Kds.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemCocina;

    private String nombrePlato;
    private Integer cantidad;
    private String estado; // Ejemplo: "Pendiente", "En Preparación", "Listo"
}