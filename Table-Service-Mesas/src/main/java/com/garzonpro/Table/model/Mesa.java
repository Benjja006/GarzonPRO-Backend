package com.garzonpro.Table.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesa")
@Data
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Long idMesa;

    @Column(name = "nombre_mesa", nullable = false, length = 50)
    private String nombreMesa;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EnumTableStatus estado = EnumTableStatus.LIBRE;
}