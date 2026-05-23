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
    private String rol;


    public UsuarioRequestDTO() {}

    // === GETTERS Y SETTERS MANUALES (Para eliminar el rojo de raíz) ===
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

}