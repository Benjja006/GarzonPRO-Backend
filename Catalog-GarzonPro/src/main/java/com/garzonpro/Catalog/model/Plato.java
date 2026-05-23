package com.garzonpro.Catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plato")
@Data
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plato")
    private Long idPlato;

    @Column(name = "nombre_plato")
    private String nombrePlato;

    private Double precio;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}