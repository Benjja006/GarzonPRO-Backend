package com.garzonpro.Auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "credencial")
@Data
public class Credencial {
    @Id
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El PIN es obligatorio")
    @Size(min = 4, max = 4, message = "El PIN debe ser de 4 dígitos")
    @Column(name = "pin_usuario") // Asegura coincidencia con la DB
    private String pinUsuario;

    @Column(name = "token_sesion")
    private String tokenSesion;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Column(name = "id_usuario")
    private Long idUsuario;
}