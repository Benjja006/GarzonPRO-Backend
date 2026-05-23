package com.garzonpro.Reporting.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CierreCajaResponseDTO {

    private Long idCierre;
    private LocalDate fecha;
    private Double totalVentasDia;
    private Integer cantidadPedidos;
}