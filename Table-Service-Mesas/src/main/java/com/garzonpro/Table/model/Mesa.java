package com.garzonpro.Table.model;

import jakarta.persistence.*;
import lombok.Data;
import com.garzonpro.Table.model.EnumTableStatus;

@Entity
@Data
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    private String nombreMesa;

    @Enumerated(EnumType.STRING)
    private EnumTableStatus estado = EnumTableStatus.LIBRE;
}