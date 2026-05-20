package com.garzonpro.User.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data // Genera Getters y Setters automáticamente con Lombok
public class Usuario {

    @Id
    @Column(name = "id_usuario") // Por si acaso en la base de datos se usa snake_case
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El rol es obligatorio (GARZON, COCINA, ADMIN)")
    private String rol;

    @Email(message = "El formato del email no es válido")
    @Column(name = "correo") // 🌟 SOLUCIÓN: Mapea la variable 'email' a la columna 'correo' de MySQL
    private String email;

    private Boolean activo = true;
}