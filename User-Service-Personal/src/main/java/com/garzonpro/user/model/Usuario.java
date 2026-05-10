package com.garzonpro.user.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data // Genera Getters y Setters automáticamente con Lombok
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El rol es obligatorio (GARZON, COCINA, ADMIN)")
    private String rol;

    @Email(message = "El formato del email no es válido")
    private String email;

    private Boolean activo = true;
}