package com.garzonpro.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El PIN es obligatorio")
    @Size(min = 4, max = 4, message = "El PIN debe tener exactamente 4 dígitos")
    private String pin;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "El formato del correo es inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    private String rol;
    private Long idUsuario;
}