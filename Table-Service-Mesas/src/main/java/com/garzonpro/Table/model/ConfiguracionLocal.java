package com.garzonpro.Table.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "configuracion_local")
@Data
public class ConfiguracionLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion") // Esto le dice a Hibernate que busque 'id_configuracion'
    private Long idConfiguracion;

    @Column(name = "nombre_parametro", nullable = false)
    private String nombreParametro;

    @Column(name = "valor_parametro")
    private String valorParametro;

    private Integer aforoMaximo;
    private String horarioApertura;
    private String horarioCierre;
}