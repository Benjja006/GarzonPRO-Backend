package com.garzonpro.Auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El PIN es obligatorio")
    @Size(min = 4, max = 4, message = "El PIN debe ser de 4 dígitos")
    private String pinUsuario;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
}