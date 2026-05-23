package com.garzonpro.Auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "credencial")
public class Credencial {

    @Id
    private String username;

    @Column(name = "pin_usuario", nullable = false)
    private String pinUsuario;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "token_sesion")
    private String tokenSesion;

    public Credencial() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPinUsuario() { return pinUsuario; }
    public void setPinUsuario(String pinUsuario) { this.pinUsuario = pinUsuario; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getTokenSesion() { return tokenSesion; }
    public void setTokenSesion(String tokenSesion) { this.tokenSesion = tokenSesion; }
}