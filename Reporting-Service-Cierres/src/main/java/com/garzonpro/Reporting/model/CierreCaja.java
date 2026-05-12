package com.garzonpro.Reporting.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class CierreCaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCierre;
    private LocalDate fecha = LocalDate.now();
    private Double totalVentasDia;
    private Integer cantidadPedidos;
}