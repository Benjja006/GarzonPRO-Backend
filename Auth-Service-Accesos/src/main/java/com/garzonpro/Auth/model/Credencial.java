package com.garzonpro.Auth.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "credencial")
@Data
public class Credencial {
    @Id
    private String username;

    @Column(name = "pin_usuario")
    private String pinUsuario;

    @Column(name = "token_sesion")
    private String tokenSesion;

    @Column(name = "id_usuario")
    private Long idUsuario;
}