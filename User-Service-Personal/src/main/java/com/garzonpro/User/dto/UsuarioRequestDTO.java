package com.garzonpro.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "El formato del correo es inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    // Este ID debe coincidir con el que viene de Auth para vincularlos
    private Long idUsuario;
}