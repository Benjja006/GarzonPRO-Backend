package com.garzonpro.Table.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MesaDTO {
    @NotBlank(message = "El nombre de la mesa es obligatorio")
    private String nombreMesa;
}