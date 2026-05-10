package com.garzonpro.Catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "platos")
@Data
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlato;

    private String nombrePlato;
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}