package com.garzonpro.Table.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "configuracion_local")
@Data
public class ConfiguracionLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConfiguracion;

    private Integer aforoMaximo;
    private String horarioApertura;
    private String horarioCierre;
}